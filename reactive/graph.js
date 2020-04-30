const Subject = require('rxjs').Subject
class Graph{
    constructor(){
        this.nodes = new Set()
        this.edges = new Set()
        this.s = new Subject()
    }

    update(nodes, edges){
        this.addNodes(nodes)
        for(let i = 0; i < edges.length; i++){
            this.addEdge(edges[i])
        }
        this.s.next([this.nodes, this.edges, nodes, edges])
    }

    addNodes(nodes){
        for(let i = 0; i < nodes.length; i ++){
            this.nodes.add(nodes[i])
        }
    }

    addEdge(e){
        if(this.nodes.has(e[0]) && this.nodes.has(e[1])){
            this.edges.add(e)
        } else {
            throw "graph hasn't one of this nodes " + e
        }
    }

    listenUpdate(callback){
        this.s.subscribe(callback)
    }
}

module.exports = Graph