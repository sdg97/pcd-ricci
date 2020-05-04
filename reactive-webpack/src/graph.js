var ForceGraph3D = require('3d-force-graph').default
var SpriteText = require('three-spritetext').default
var THREE = require('three')

class Graph {
    constructor(div) {
        const initData = {
            nodes: [],
            links: []
        };

        this.Graph = ForceGraph3D()(div)
            .nodeAutoColorBy('group')
            .nodeThreeObject(node => {
                // use a sphere as a drag handle
                const obj = new THREE.Mesh(
                    new THREE.SphereGeometry(10),
                    new THREE.MeshBasicMaterial({ depthWrite: false, transparent: true, opacity: 0 })
                );

                // add text sprite as child
                const sprite = new SpriteText(node.id);
                sprite.color = node.color;
                sprite.textHeight = 8;
                obj.add(sprite);

                return obj;
            })
            .graphData(initData);
    }

    checkNode(node, nodes){
        console.log(nodes.filter(n => {console.log(n.id, node);return n.id = node}).length == 0)
        if(! nodes.filter(n => {return n.id == node}).length == 0){
            console.log('NON VA', )
            console.log(nodes.filter(n => {console.log(n.id,node);return n.id == node}))
        }
        return nodes.filter(n => {return n.id == node}).length == 0
    }

    addNode(node) {
        console.log('graph', node)

        const { nodes, links } = this.Graph.graphData();
        if(this.checkNode(node, nodes)){
            this.Graph.graphData({
                nodes: [...nodes, { id: node }],
                links: links
            });
        } 

        console.log('nodi',nodes)
    }

    changeColor() {

    }

    addLink(source, target) {
        const { nodes, links } = this.Graph.graphData();
        this.Graph.graphData({
            nodes: nodes,
            links: [...links, { source: source, target: target }]
        });

        console.log('link', links)
    }
}

module.exports = Graph