import React from 'react';
import { TextAreaAddress } from "./TextAreaAddres";
import { GraphVisualizer } from "./GraphVisualizer";
const Graph = require('../Model/graph')
const axios = require('axios').default;

const  myConfig = {
    nodeHighlightBehavior: true,
    node: {
        color: "lightgreen",
        size: 120,
        highlightStrokeColor: "blue",
    },
    link: {
        highlightColor: "lightblue",
    }
  }
export class Main extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            nodes : [{id : 'cacca'}],
            edges : [],
            counter : 0
        }
        this.createUrlForPageInfo = this.createUrlForPageInfo.bind(this)
        this.getPageTitleFromUrl = this.getPageTitleFromUrl.bind(this)
        this.checkIfIsARealLink = this.checkIfIsARealLink.bind(this)
        this.getPageReference = this.getPageReference.bind(this)
        this.search = this.search.bind(this)
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
        return "https://it.wikipedia.org/w/api.php?action=parse&page="+ title+"&format=json&section=0&prop=links"
    }

    getPageTitleFromUrl(url){
        url.trim()
        let urlArray = url.split('/')
        return urlArray[urlArray.length - 1]    
    }

    checkIfIsARealLink(pseudoLink){
        return pseudoLink['ns'] == 0
    }

    async getPageReference(title){
        var config = {
            headers: {'Access-Control-Allow-Origin': '*'},
            proxy: {
                host: '104.236.174.88',
                port: 3128
            }
        };
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

    async search(title, currDepth, maxDepth){
        console.log(title, currDepth, maxDepth)
        if(currDepth < maxDepth){
            let res = await this.getPageReference(title)
            /*if(currDepth == 1) {
                console.log('Risultati fase 2' , title , res)
            }*/   
            //aggiuge i nodi
            console.log(res)
            for(let i = 0; i < res.length; i ++){
                const tempRes = res[i]
                this.setState({
                    nodes: [ ...this.state.nodes, {id : tempRes}]
                })
                this.setState({
                    edges: [ ...this.state.edges, {source : title, target : tempRes}]
                })
            }            //aggiunge gli archi
            let newCount = this.state.counter + 1
            this.setState({counter : newCount})
            //continua la ricerca a un livello più profondo
            for(let i = 0; i < res.length; i ++){
                this.search(res[i], currDepth + 1, maxDepth)
            }
        } 
    }

    
}