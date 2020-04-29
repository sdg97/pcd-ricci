import React from 'react';
import { Graph } from "react-d3-graph";

export class GraphVisualizer extends React.Component {
    constructor(props){
        super(props);
    }

    render() {
        console.log({nodes : this.props.nodes, links: this.props.edges});
        
        return <Graph
        id="graph-id" // id is mandatory, if no id is defined rd3g will throw an error
        data={{nodes : this.props.nodes, links: this.props.edges}}
        config={this.props.config}
    />;
    }


}