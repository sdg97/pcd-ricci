import React from 'react';
import { Graph } from "react-d3-graph";

export class GraphVisualizer extends React.Component {
    render() {
        if(this.props.nodes.length !== 0){
            return (
            <div>
                <label>{this.props.nodes.length}</label>
                <Graph
                        id="graph-id" // id is mandatory, if no id is defined rd3g will throw an error
                        data={{nodes : this.props.nodes, links: this.props.edges}}
                        config={this.props.config}
                />;
            </div>)
        
        } else{
            return <label>there is no data yet</label>
        }
    }
}