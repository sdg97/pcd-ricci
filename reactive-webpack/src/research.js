const defer = require('rxjs').defer
const axios = require('axios').default

class Research {
    constructor(mainTitle, maxDepth, currDepth, source, target, subject) {
        this.mainTitle = mainTitle
        this.maxDepth = maxDepth
        this.currDepth = currDepth
        this.source = source
        this.target = target
        this.subject = subject
    }

    createUrlForPageInfo() {
        console.log()
        return "https://it.wikipedia.org/w/api.php?action=parse&page=" + this.target + "&format=json&section=0&prop=links"
    }

    checkIfIsARealLink(pseudoLink) {
        return pseudoLink['ns'] === 0
    }

    reactiveGetPageReference() {
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
            let ref = []
            let res = await axios.get(this.createUrlForPageInfo(), config)
            let link = res.data.parse.links
            for (let i = 0; i < link.length; i++) {
                if (this.checkIfIsARealLink(link[i]))
                    ref.push(link[i]['*'])
            }
            return ref
        })
    }

    reactiveSearch() {
        this.subject.next({
            source : this.source,
            target: this.target,
            mainTitle: this.mainTitle

        })
        if (this.currDepth < this.maxDepth) {
            this.reactiveGetPageReference().subscribe((res) => {
                for (let i = 0; i < res.length; i++) {
                    let r = new Research(this.mainTitle, this.maxDepth, this.currDepth + 1, this.target, res[i], this.subject)
                    r.reactiveSearch()
                }
            }, (err) => {
                console.log('CANNOT GET THE REQUESTED PAGE')
            })
        }

    }
}

module.exports = Research