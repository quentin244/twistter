import React from 'react';
import {Login} from './Login';
import {Logout} from './Logout';
import axios from 'axios';
import {Inscription} from './inscription'
import { Principal } from './Principal';
export class NavigationPanel extends React.Component{
    constructor(props){
        super(props)
        this.state={current : this.props.current}
        this.login = this.props.login;
        this.logout = this.props.logout;
        this.inscription = this.props.inscription;
    }
    render(){
        if (this.props.isConnected == false) {
            if(this.props.account == false){
                return(
                    <nav>
                        <Inscription ins></Inscription>
                    </nav>
                )
            }
            else{
                return (
                    <nav>
                        <Login lo={this.login}
                               ins={this.inscription}/>
                    </nav>);
            }
        }
        else{
            return (
            <nav>
                <Principal lo={this.logout}
                           ins={this.inscription}/>
            </nav>
            );
        }
    }
}