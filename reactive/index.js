const axios = require('axios').default;
const Graph = require('./graph')
const defer = require('rxjs').defer

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
function getPageReference(title){
    return defer(async () =>{
        let res = await axios.get(createUrlForPageInfo(title))
        let ref = []
        let link = res.data.parse.links
        for (let i = 0; i < link.length; i++){
            if(checkIfIsARealLink(link[i]))
            ref.push(link[i]['*'])
        }
        return ref
    })
}

function search(title, currDepth, maxDepth, graph){

    let subscriber = {
        next(res){
             //crea gli archi
            let edges = []
            for(let i = 0; i < res.length; i ++){
                edges.push([title, res[i]])
            }
            
            // aggiorna il grafo
            graph.update(res, edges)
            
            //continua la ricerca a un livello più profondo
            for(let i = 0; i < res.length; i ++){
                if(currDepth < maxDepth){
                    //console.log('currDepth ' + currDepth + ' maxDepth ' + maxDepth )
                    search(res[i], currDepth + 1, maxDepth, graph)
                }
            }
        }, 

        error(err){
            console.log('ERROR GETTING WORLD', title)
        }
        
    }
   
    getPageReference(title).subscribe(subscriber)
}

let title = getPageTitleFromUrl(url)
graph.listenUpdate((update) => {
    console.log('nodi', update[2])
    console.log('archi', update[3])
})
graph.update([title], [])
search(title, 0, depth, graph)
