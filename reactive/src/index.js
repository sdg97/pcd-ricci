const Graph = require('./graph')
const Subject = require('rxjs').Subject
const takeUntil = require('rxjs/operators').takeUntil
const Research = require('./research')
const intervalTimer = 20000
const researcheInterval = []
let graph
let reciver
let startClear = new Subject() 
const nodeNumberLabelId = "n_nodes"
const graphDivId = "graph"

start()


function settings(){
  researcheInterval.forEach((i) => {
    clearInterval(i)
  })
  let label =  document.getElementById(nodeNumberLabelId)
  label.innerHTML = 0
  reciver = new Subject()
  graph = new Graph(document.getElementById(graphDivId))
  reciver.subscribe((res) =>{
    if(res.mainTitle == res.target){
      graph.addNode(res.target, res.mainTitle)
    } else {
      graph.addNode(res.source, res.mainTitle)
      graph.addNode(res.target, res.mainTitle)
      graph.addLink(res.source, res.target)
    }

    label.innerHTML = graph.getNodesNumber()
  })

  reciver.pipe(takeUntil(startClear))
}

function start() {
  document.addEventListener('DOMContentLoaded', function () {
   settings()
  }, false);
}


function getPageTitleFromUrl(url) {
  url.trim()
  let urlArray = url.split('/')
  return urlArray[urlArray.length - 1]
}

function startSearch() {
  let URL = document.getElementById("URL").value
  let depth = document.getElementById("depth").value

  let title = getPageTitleFromUrl(URL)

  let r = new Research(title, depth, 0, '', title, reciver)

  let interval = setInterval(() => {
    r.reactiveSearch()
  }, intervalTimer)

  researcheInterval.push(interval)
  r.reactiveSearch()
}

function clear(){
  startClear.next(true)
  settings()
}


module.exports = {
  startSearch, clear
}
