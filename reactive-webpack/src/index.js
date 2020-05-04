window.jQuery = require('jquery');
require('bootstrap');
require('font-awesome/css/font-awesome.css');
var ForceGraph3D = require('3d-force-graph').default


function startSearch() {
  const initData = {
    nodes: [ {id: 0 } ],
    links: []
  };
  let div = document.getElementById("graph");
  console.log(div)
  var Graph = ForceGraph3D()(div).graphData(initData);

  setInterval(() => {
    console.log('interval')
    const { nodes, links } = Graph.graphData();
    const id = nodes.length;
    Graph.graphData({
      nodes: [...nodes, { id }],
      links: [...links, { source: id, target: Math.round(Math.random() * (id-1)) }]
    });
  }, 1000);
}


module.exports = {
  startSearch,
}
