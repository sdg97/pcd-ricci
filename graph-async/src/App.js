import React from 'react';
import './App.css';
import { GraphVisualizer } from "./Components/GraphVisualizer";
import { TextAreaAddress } from "./Components/TextAreaAddres";
import { Main } from "./Components/Main";
let data = {
  nodes: [{ id: "Harry" }, { id: "Sally" }, { id: "Alice" }],
  links: [
      { source: "Harry", target: "Sally" },
      { source: "Harry", target: "Alice" },
  ],
};


function App() {
  return (
    <div className="App" >
      <Main></Main>
    </div>
  );
}

export default App;
