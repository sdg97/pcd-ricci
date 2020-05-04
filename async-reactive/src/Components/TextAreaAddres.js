import React from 'react';
import Select from '@material-ui/core/Select';
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
const refreshTimeout = 20000

class Research {
  constructor() {
    this.mode = undefined
    this.mainTitle = undefined
    this.interval = undefined
    this.depth = undefined
  }
}
export class TextAreaAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = { value: '', depth: 0, mode: 'async', interval: null, mainTitle: '', researches: [] };
    this.handleValueChange = this.handleValueChange.bind(this);
    this.handleDepthChange = this.handleDepthChange.bind(this)
    this.handleModeChange = this.handleModeChange.bind(this)
    this.onPlay = this.onPlay.bind(this)
    this.clear = this.clear.bind(this)
  }

  handleValueChange(event) {
    this.setState({ value: event.target.value });
  }

  handleDepthChange(event) {
    this.setState({ depth: event.target.value });
  }

  handleModeChange(event) {
    this.setState({ mode: event.target.value })
  }

  getPageTitleFromUrl(url) {
    url.trim()
    let urlArray = url.split('/')
    return urlArray[urlArray.length - 1]
  }

  onPlay() {
    let research = new Research()
    this.setState((state) => {
      research.mainTitle = this.getPageTitleFromUrl(state.value)
      research.mode = state.mode
      research.depth = state.depth
      research.interval = setInterval(() => {
        console.log('refresh', research.mainTitle)
        this.startResearch(research)
      }, refreshTimeout)

      return {researches : [...state.researches, research]}
    }, () => this.startResearch(research))

  }


startResearch(research) {
  console.log('start', research.mainTitle)
  if (research.mode == 'async') {
    this.props.handleSubmitAsync(research.mainTitle, 0, research.depth)
  } else {
    this.props.handleSubmitReactive(research.mainTitle, 0, research.depth)
  }
}

clear(){
  window.location.reload(false);

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
            <input size="3" type="number" onChange={this.handleDepthChange} />
      </label>
      <FormControl variant="filled">
        <InputLabel htmlFor="filled-age-native-simple">Mode</InputLabel>
        <Select
          native
          onChange={this.handleModeChange}
          inputProps={{
            name: 'age',
            id: 'filled-age-native-simple',
          }}
        >
          <option aria-label="None" value="" />
          <option value={'async'}>Async</option>
          <option value={'reactive'}>Reactve</option>
        </Select>
      </FormControl>
      <button type="submit" onClick={this.onPlay}>Play</button>
      <button onClick={this.clear}>Clear</button>
    </div>
  )
}
}