import React from 'react';
import { TextAreaAddress } from "./TextAreaAddres";
import { GraphVisualizer } from "./GraphVisualizer";
import { defer } from "rxjs";
const axios = require('axios').default;

const myConfig = {
    nodeHighlightBehavior: true,
    node: {
        color: "lightgreen",
        size: 1500,
        highlightStrokeColor: "blue",
    },
    link: {
        type: "CURVE_SMOOTH",
        highlightColor: "lightblue",
    },
    directed: true,
    height: window.innerHeight,
    width: window.innerWidth
}
export class AsyncMain extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            nodes: [],
            edges: [],
            counter: 0
        }
        this.createUrlForPageInfo = this.createUrlForPageInfo.bind(this)
        this.getPageTitleFromUrl = this.getPageTitleFromUrl.bind(this)
        this.checkIfIsARealLink = this.checkIfIsARealLink.bind(this)
        this.asyncGetPageReference = this.asyncGetPageReference.bind(this)
        this.asyncSearch = this.asyncSearch.bind(this)
        this.reactiveSearch = this.reactiveSearch.bind(this)
        this.addEdge = this.addEdge.bind(this)
        this.addNode = this.addNode.bind(this)
    }

    render() {
        return (
            <div>
                <TextAreaAddress handleSubmitAsync={this.asyncSearch} handleSubmitReactive={this.reactiveSearch}></TextAreaAddress>
                <GraphVisualizer nodes={this.state.nodes} edges={this.state.edges} config={myConfig}></GraphVisualizer>
            </div>
        )
    }

    createUrlForPageInfo(title) {
        //https://cors-anywhere.herokuapp.com/
        return "https://it.wikipedia.org/w/api.php?action=parse&page=" + title + "&format=json&section=0&prop=links"
    }

    getPageTitleFromUrl(url) {
        url.trim()
        let urlArray = url.split('/')
        return urlArray[urlArray.length - 1]
    }

    checkIfIsARealLink(pseudoLink) {
        return pseudoLink['ns'] === 0
    }

    async asyncGetPageReference(title) {
        var config = {
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:3000',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
                'Access-Control-Allow-Credentials': true,
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }
        let res = await axios.get(this.createUrlForPageInfo(title), config)

        let ref = []
        let link = res.data.parse.links
        for (let i = 0; i < link.length; i++) {
            if (this.checkIfIsARealLink(link[i]))
                ref.push(link[i]['*'])
        }
        return ref
    }


    addNode(node) {
        setTimeout(() => {
            this.setState({
                nodes: [...this.state.nodes, { id: node }]
            })
        }, 1000);
    }

    addEdge(source, target) {
        setTimeout(() => {
            this.setState({
                edges: [...this.state.edges, { source: source, target: target }]
            })
        }, 1000);
    }

    async asyncSearch(title, currDepth, maxDepth) {
        console.log('current Depth', currDepth)

        if (currDepth === 0) {
            console.log("Async");
            this.addNode(title)
        }

        if (currDepth < maxDepth) {
            let res = await this.asyncGetPageReference(title);
            let newCount = this.state.counter + 1
            this.setState({ counter: newCount })
            for (let i = 0; i < res.length; i++) {
                this.addNode(res[i])
            }
            for (let i = 0; i < res.length; i++) {
                this.addEdge(title, res[i])
            }

            for (let i = 0; i < res.length; i++) {
                this.asyncSearch(res[i], currDepth + 1, maxDepth)
            }
        }
    }

    reactiveGetPageReference(title) {
        var config = {
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:3000',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
                'Access-Control-Allow-Credentials': true,
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }
        return defer(async () => {
            let res = await axios.get(this.createUrlForPageInfo(title), config)
            let ref = []
            let link = res.data.parse.links
            for (let i = 0; i < link.length; i++) {
                if (this.checkIfIsARealLink(link[i]))
                    ref.push(link[i]['*'])
            }
            return ref
        })
    }

    reactiveSearch(title, currDepth, maxDepth) {
        console.log('current Depth', currDepth)
        if (currDepth === 0) {
            console.log("Reactive");
            this.addNode(title)
        }
        if (currDepth < maxDepth) {
            this.reactiveGetPageReference(title).subscribe((res) => {
                for (let i = 0; i < res.length; i++) {
                    this.addNode(res[i])
                }
                for (let i = 0; i < res.length; i++) {
                    this.addEdge(title, res[i])
                }
                for (let i = 0; i < res.length; i++) {
                    this.reactiveSearch(res[i], currDepth + 1, maxDepth)

                }

            })
        }
    }


}