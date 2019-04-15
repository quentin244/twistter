import React from 'react';
import axios from 'axios';
import { Login } from './Login';
import { error } from 'util';
export class Inscription extends React.Component{
    constructor(props){super(props)}
    render(){
        return (
            <div>
                <header>
                    <h1>Créer un compte</h1>
                </header>
                <div className='Inscription' id="inscription">
                    <ul className="champ">
                        <li>
                            <ul className="ligne_1">
                                <li className="colonne">
                                    <span>Prénom</span>
                                    <input id="id_input_1" ref="prenom"></input>
                                </li>
                                <li className="colonne">
                                    <span>Nom</span>
                                    <input id="id_input_2" ref="nom"></input>
                                </li>
                            </ul>
                        </li>
                        <ul className="code_info">
                            <li>
                                <span className="left_side"> Login </span>
                                <input className="code_input" ref="login"></input>
                            </li>
                            <li>
                                <span className="left_side"> EMail </span>
                                <input className="code_input" ref="mail"></input>
                            </li>
                            <li>
                                <span className="left_side"> Mot de passe </span>
                                <input className="code_input" type="password" ref="password"></input>
                            </li>
                            <li>
                                <span className="left_side"> Confirmer </span>
                                <input className="code_input" type="password"></input>
                            </li>
                            <li className="bouton">
                                <button onClick={()=>this.send()}>Enregistrer</button>
                                <button>Annuler</button>
                            </li>
                        </ul>
                    </ul>
                </div>
            </div>
        )
    }

    send(){
        var formData = new URLSearchParams();
        formData.append("login", this.refs.login.value);
        formData.append("password", this.refs.password.value);
        formData.append("nom", this.refs.nom.value);
        formData.append("prenom", this.refs.prenom.value);
        formData.append("mail", this.refs.mail.value);
        axios.get("http://localhost:8080/3i017-PTW/servlet/CreateUser?"+formData.toString()).then(response=>{alert("Resultat : "+ response.data["message"])}).catch(error=>{alert(error)})
    }
}