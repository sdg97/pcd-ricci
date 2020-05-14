import React from 'react';
import { TextAreaAddress } from "./TextAreaAddres";
import { GraphVisualizer } from "./GraphVisualizer";
const axios = require('axios').default;

const myConfig = {
    nodeHighlightBehavior: true,
    node: {
        color: "lightgreen",
        size: 900,
        highlightStrokeColor: "blue",
    },
    link: {
        type: "CURVE_SMOOTH",
        highlightColor: "lightblue",
        markerHeight: 20,
        markerWidth: 10
    },
    directed: true,
    height: window.innerHeight,
    width: window.innerWidth
}
export class Main extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            nodes: [],
            edges: [],
            counter: 0,
        }
        this.createUrlForPageInfo = this.createUrlForPageInfo.bind(this)
        this.checkIfIsARealLink = this.checkIfIsARealLink.bind(this)
        this.asyncGetPageReference = this.asyncGetPageReference.bind(this)
        this.asyncSearch = this.asyncSearch.bind(this)
        this.addEdge = this.addEdge.bind(this)
        this.addNode = this.addNode.bind(this)
    }

    render() {
        return (
            <div>
                <TextAreaAddress handleSubmitAsync={this.asyncSearch} ></TextAreaAddress>
                <GraphVisualizer nodes={this.state.nodes} edges={this.state.edges} config={myConfig}></GraphVisualizer>
            </div>
        )
    }


    createUrlForPageInfo(title) {
        //https://cors-anywhere.herokuapp.com/
        return "https://it.wikipedia.org/w/api.php?action=parse&page=" + title + "&format=json&section=0&prop=links"
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
        let ref = []

        try {
            let res = await axios.get(this.createUrlForPageInfo(title), config)

            let link = res.data.parse.links
            for (let i = 0; i < link.length; i++) {
                if (this.checkIfIsARealLink(link[i]))
                    ref.push(link[i]['*'])
            }
        } catch (error) {
            console.log('CANNOT GET THE REQUESTED PAGE')
        }
        return ref
    }

    canInsertNode(node, nodes){
        for(let i = 0; i < nodes.length; i++){
            if(nodes[i].id == node){
                return false
            }
        }
        return true
    }

    canInsertEdges(source, target, edges){
        for(let i = 0; i < edges.length; i++){
            if(edges[i].source == source && edges[i].target == target){
                return false
            }
        }

        return true
    }

    addNode(node) {
        setTimeout(() => {
            this.setState((state) => {
                if (this.canInsertNode(node,state.nodes)) {
                    let nodes = [...state.nodes, { id: node }]
                    return {
                        nodes: nodes,
                        counter: nodes.length
                    }
                }
            })
        }, 1000);
    }

    addEdge(source, target) {
        setTimeout(() => {
            this.setState((state) => {
                if ( this.canInsertEdges(source, target, state.edges)) {
                    let edges = [...state.edges, { source: source, target: target }]
                    return {
                        edges: edges
                    }
                }

            })
        }, 1000);
    }


    async asyncSearch(title, currDepth, maxDepth) {

        if (currDepth === 0) {
            this.addNode(title)
        }

        if (currDepth < maxDepth) {
            let res = await this.asyncGetPageReference(title);

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

}