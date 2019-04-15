import React from 'react';
import axios from 'axios';
export class Logout extends React.Component{
    constructor(props){
        super(props);
        this.logout = this.props.lo;
    }
    render(){
        return <button onClick={()=>this.logout()}>Deconnexion</button>
    }
}