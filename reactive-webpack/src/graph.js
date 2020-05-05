var ForceGraph3D = require('3d-force-graph').default
var SpriteText = require('three-spritetext').default
var THREE = require('three')

class Graph {
    constructor(div, label) {
        this.currentGroup
        this.label = label
        const initData = {
            nodes: [],
            links: []
        };

        this.Graph = ForceGraph3D()(div)
            .nodeAutoColorBy('group')
            .nodeThreeObject(node => {
                const obj = new THREE.Mesh(
                    new THREE.SphereGeometry(10),
                    new THREE.MeshBasicMaterial({ depthWrite: false, transparent: true, opacity: 0 })
                );

                const sprite = new SpriteText(node.id);
                sprite.color = node.color;
                sprite.textHeight = 8;
                obj.add(sprite);

                return obj;
            })
            .graphData(initData)
            .linkDirectionalArrowLength(3.5)
            .linkDirectionalArrowRelPos(1)
    }

    getNodesNumber(){
        const { nodes, links } = this.Graph.graphData();
        return nodes.length
    }

    checkNode(node, nodes) {
        return nodes.filter(n => { return n.id == node }).length == 0
    }

    addNode(node, group) {
        const { nodes, links } = this.Graph.graphData();
        if (this.checkNode(node, nodes)) {
            this.Graph.graphData({
                nodes: [...nodes, { id: node, group: group }],
                links: links
            });
        }
    }

    checkLink(source, target, links) {

        return links.filter(l => { 
            return l.source == source && l.target == target 
        }).length == 0


    }

    addLink(source, target) {
        const { nodes, links } = this.Graph.graphData();
        if(this.checkLink(source, target, links)){
            this.Graph.graphData({
                nodes: nodes,
                links: [...links, { source: source, target: target }]
            });
        }
    }
}

module.exports = Graph