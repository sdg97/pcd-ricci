import React from 'react';
import Select from '@material-ui/core/Select';
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
const refreshTimeout = 20000
export class TextAreaAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = { value: '', depth: 0, mode: 'async', interval: null, mainTitle: '' };
    this.handleValueChange = this.handleValueChange.bind(this);
    this.handleDepthChange = this.handleDepthChange.bind(this)
    this.handleModeChange = this.handleModeChange.bind(this)
    this.onPlay = this.onPlay.bind(this)
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
    let title = this.getPageTitleFromUrl(this.state.value)
    this.setState({ mainTitle: title },
      () => {
        if (this.state.interval != null) {
          clearInterval(this.state.interval)
        }

        let interval = setInterval(() => {
          this.startResearch()
        }, refreshTimeout)

        this.setState({
          interval: interval
        })

        this.startResearch()
      })

  }

  startResearch() {
    if (this.state.mode == 'async') {
      this.props.handleSubmitAsync(this.state.mainTitle, 0, this.state.depth)
    } else {
      this.props.handleSubmitReactive(this.state.mainTitle, 0, this.state.depth)
    }
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
      </div>

    )
  }
}