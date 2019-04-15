import React from 'react';
import {NavigationPanel} from './NavigationPanel';
import axios from 'axios';
export class MainPage extends React.Component{
    constructor(props){
        super(props);
        this.state={connected : true, account : true, currentPage : "inscription"};
        this.getConnected = this.getConnected.bind(this);
        this.setLogout = this.setLogout.bind(this);
        this.inscription = this.inscription.bind(this);
    }
    render(){
        console.log(this.state.connected)
        return (<div className="MainPage">
                <NavigationPanel login={this.getConnected} 
                                 logout={this.setLogout} 
                                 isConnected={this.state.connected}
                                 account={this.state.account}
                                 inscription={this.inscription}>
                </NavigationPanel>
            </div>)
    }
    getConnected(){this.setState({connected : true, currentPage : "principal"})}
    setLogout(){this.setState({connected : false, currentPage : "connexion"})}
    inscription(){this.setState({connected : false, account : false, currentPage : "inscription"})}
}