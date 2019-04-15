import React from 'react';
import axios from 'axios';
import {Inscription} from './inscription'
export class Login extends React.Component{
    constructor(props){
        super(props)
        this.login = this.props.lo;
        this.ins = this.props.ins;
    }
    render(){
        return (
        <div>
            <header>
                <h1>Connexion</h1>
            </header>
            <div className="Login" id="connexion">
                <ul>
                    <li>
                        <span class="left_side"> Login </span>
                        <input class="rigth_side" ref="login"></input>
                    </li>
                    <li>
                        <span class="left_side"> Mot de Passe </span>
                        <input type="password" ref="password"></input>
                    </li>
                    <button onClick={()=>this.send()}>Connexion</button>
                    <li>
                        <span class="left_side">
                        <button>Mot de passe perdu</button>
                        </span>
                        <span class="right_side">
                        <button onClick={()=>this.ins()}>Pas encore inscrit ?</button>
                        </span>
                    </li>
                </ul>
            </div>
        </div>)
    }

    send(){
        var formData = new URLSearchParams();
        formData.append("login", this.refs.login.value);
        formData.append("password", this.refs.password.value);
        axios.get("http://localhost:8080/3i017-PTW/servlet/Login?"+formData.toString()).then(response=>{alert(response.data["message"]); if(response.data["Key"]){this.login()}}).catch(error=>{alert(error)})
    }
}