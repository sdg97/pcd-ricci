const axios = require('axios').default;
const Graph = require('./graph')
const graph = new Graph()
const url = process.argv.slice(2)[0]
const depth =  process.argv.slice(2)[1]


function createUrlForPageInfo(title){
    return "https://it.wikipedia.org/w/api.php?action=parse&page="+ title+"&format=json&section=0&prop=links"
}

function getPageTitleFromUrl(url){
    url.trim()
    let urlArray = url.split('/')
    return urlArray[urlArray.length - 1]    
}

function checkIfIsARealLink(pseudoLink){
    return pseudoLink['ns'] == 0
}

/**
 * Return an array of reference
 * @param {} title 
 */
async function getPageReference(title){
    let res = await axios.get(createUrlForPageInfo(title))
    let ref = []
    let link = res.data.parse.links
    for (let i = 0; i < link.length; i++){
        if(checkIfIsARealLink(link[i]))
        ref.push(link[i]['*'])
    }
    return ref
}

/**
 * Cerca i riferimenti e li aggiunge al grafo
 * @param {*} title 
 * @param {*} currDepth 
 */
async function search(title, currDepth, maxDepth, graph){

    if(currDepth < maxDepth){
        let res = await getPageReference(title)
        /*if(currDepth == 1) {
            console.log('Risultati fase 2' , title , res)
        }*/   
        //aggiuge i nodi
        graph.addNodes(res)
        //aggiunge gli archi
        for(let i = 0; i < res.length; i ++){
            graph.addEdge([title, res[i]])
        }
        graph.update()
        //continua la ricerca a un livello più profondo
        for(let i = 0; i < res.length; i ++){
            search(res[i], currDepth + 1, maxDepth, graph)
        }
    } 
}

let title = getPageTitleFromUrl(url)
graph.addNodes([title])
search(title, 0, depth, graph)


