import React from 'react';
import { TextAreaAddress } from "./TextAreaAddres";
import { GraphVisualizer } from "./GraphVisualizer";
const axios = require('axios').default;

const  myConfig = {
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
    height:  window.innerHeight,
    width: window.innerWidth
  }
export class Main extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            nodes : [],
            edges : [],
            counter : 0
        }
        this.createUrlForPageInfo = this.createUrlForPageInfo.bind(this)
        this.getPageTitleFromUrl = this.getPageTitleFromUrl.bind(this)
        this.checkIfIsARealLink = this.checkIfIsARealLink.bind(this)
        this.getPageReference = this.getPageReference.bind(this)
        this.search = this.search.bind(this)
        this.addEdge = this.addEdge.bind(this)
        this.addNode = this.addNode.bind(this)
    }

    render(){
        return (
            <div>
                <TextAreaAddress handleSubmit={this.search}></TextAreaAddress>
                <GraphVisualizer nodes={this.state.nodes} edges={this.state.edges} config={myConfig}></GraphVisualizer>
            </div>
        )
    }

    createUrlForPageInfo(title){
        return "https://cors-anywhere.herokuapp.com/http://it.wikipedia.org/w/api.php?action=parse&page="+ title+"&format=json&section=0&prop=links"
    }

    getPageTitleFromUrl(url){
        url.trim()
        let urlArray = url.split('/')
        return urlArray[urlArray.length - 1]    
    }

    checkIfIsARealLink(pseudoLink){
        return pseudoLink['ns'] === 0
    }

    async getPageReference(title){
        var config = {
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:3000',
                'Access-Control-Allow-Methods' : 'GET,PUT,POST,DELETE,PATCH,OPTIONS', 
                'Access-Control-Allow-Credentials':true,
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }
        let res
        res = await axios.get(this.createUrlForPageInfo(title), config)

        let ref = []
        let link = res.data.parse.links
        for (let i = 0; i < link.length; i++){
            if(this.checkIfIsARealLink(link[i]))
            ref.push(link[i]['*'])
        }
        return ref
    }


    addNode(node){
        setTimeout(() => {
            this.setState({
                nodes: [ ...this.state.nodes, {id: node}]
            })          
        }, 1000);
    }

    addEdge(source, target){
        setTimeout(() => {
            this.setState({
                edges: [ ...this.state.edges, {source: source, target: target}]
            })
        }, 1000);
    }

    async search(title, currDepth, maxDepth){
        console.log(title, currDepth, maxDepth)
        if(currDepth === 0){
            console.log("SETTO PADRE");
            this.addNode(title)
          
        }
        console.log('Current depth', currDepth)

            if(currDepth < maxDepth){
                let res = await this.getPageReference(title); 
                console.log(res)
                let newCount = this.state.counter + 1
                this.setState({counter : newCount})
                for(let i = 0; i < res.length; i ++){
                    this.addNode(res[i])
                    this.addEdge(title, res[i])
                } 
                for(let i = 0; i < res.length; i ++){
                    this.search(res[i], currDepth + 1, maxDepth)
                }
            }
   
       
            //continua la ricerca a un livello più profondo
        /*
        setTimeout (() => {
            for (let index = 0; index < 5; index++) {
                    this.setState({
                        nodes: [ ...this.state.nodes, {id : 'i'+index}]
                    })
                    this.setState({
                        edges: [ ...this.state.edges, {source : 'Harry', target : 'i'+index}]
                    })
            }
            for (let i = 1; i < 5; i++) {
                for (let index = 0; index <2; index++) {
                    this.setState({
                        nodes: [ ...this.state.nodes, {id : 'index'+index}]
                    })
                    this.setState({
                        edges: [ ...this.state.edges, {source : 'i'+i, target : 'index'+index}]
                    })
                }
            }
        }, 2000);
        */
    }

    
}