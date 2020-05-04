const Graph = require('./graph')
const defer = require('rxjs').defer
const axios = require('axios').default
let graph 

function createUrlForPageInfo(title) {
  console.log('URL', "https://it.wikipedia.org/w/api.php?action=parse&page=" + title + "&format=json&section=0&prop=links")
  return "https://it.wikipedia.org/w/api.php?action=parse&page=" + title + "&format=json&section=0&prop=links"
}

function checkIfIsARealLink(pseudoLink) {
  return pseudoLink['ns'] === 0
}

function getPageTitleFromUrl(url) {
  url.trim()
  let urlArray = url.split('/')
  return urlArray[urlArray.length - 1]
}

function  reactiveGetPageReference(title) {
  var config = {
      headers: {
          'Access-Control-Allow-Origin': 'http://localhost:5000',
          'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
          'Access-Control-Allow-Credentials': true,
          'Accept': 'application/json',
          'Content-Type': 'application/json',
      }
  }
  return defer(async () => {
      console.log('subscribe:)')
      let ref = []
      let res = await axios.get(createUrlForPageInfo(title), config)
      let link = res.data.parse.links
      for (let i = 0; i < link.length; i++) {
          if (checkIfIsARealLink(link[i]))
              ref.push(link[i]['*'])
      }
      return ref
  })
}

function reactiveSearch(title, currDepth, maxDepth) {
  if (currDepth === 0) {
    console.log('aggiungo', title)
    graph.addNode(title)
  }
  if (currDepth < maxDepth) {
      reactiveGetPageReference(title).subscribe((res) => {
          for (let i = 0; i < res.length; i++) {
              graph.addNode(res[i])
          }
          for (let i = 0; i < res.length; i++) {
              graph.addLink(res[i], title)
          }
          for (let i = 0; i < res.length; i++) {
              reactiveSearch(res[i], currDepth + 1, maxDepth)

          }

      }, (err) => {
          console.log('CANNOT GET THE REQUESTED PAGE')
      })
  }
}

function startSearch() {
  graph = new Graph(document.getElementById("graph"))
  let URL = document.getElementById("URL").value
  let depth = document.getElementById("depth").value

  let title = getPageTitleFromUrl(URL)  
  
  reactiveSearch(title, 0 ,depth)
}


module.exports = {
  startSearch,
}
