import React from 'react';
export class TextAreaAddress extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: '', depth : 0};
        this.handleValueChange = this.handleValueChange.bind(this);
        this.handleDepthChange = this.handleDepthChange.bind(this)
      }
    
      handleValueChange(event) {
        this.setState({value: event.target.value});
      }

      handleDepthChange(event) {
        this.setState({depth: event.target.value});
      }
    

    
      render() {
        return (
            <div>
                    <label>
                      Address:
                      <input type="text" value={this.state.value} onChange={this.handleValueChange} />
                    </label>
                    <label>
                      Depth:
                      <input size="3" type="number"  onChange={this.handleDepthChange} />
                    </label>
                    <button type="submit" onClick={e => this.props.handleSubmit(this.state.value, 0, this.state.depth)}>Play</button>

            </div>
         
        )
      }
}