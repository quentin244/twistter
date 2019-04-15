import React from 'react';
import axios from 'axios';
import {Logout} from './Logout';
export class Principal extends React.Component{
    constructor(props){super(props)}
    render(){
        return (
        <div>
            <header>
                <img id="logo" src="Logo.jpg" alt="logo"/>
                <div class="zone_recherche">
                    <textarea id="recherche_text"></textarea>
                    <button id="recherche_button">Recherche</button>
                </div>
                    <Logout lo={this.props.lo}
                            inscription={this.props.inscription}/>
            </header>
            <div class="lecture">
                <section class="stat">
                    <p>Liste de<br/>Statistiques</p>
                </section>
                <section class="message">
                    <div class="zone_nouveau_message">
                            <textarea id="nouveau_message_text"></textarea>
                            <button id="nouveau_message_button">Envoyer</button>
                    </div>
                    <section class="liste_messages">
                        <p class="message_affiche">Message 1</p>
                        <p class="message_affiche">Message 2</p>
                        <p class="message_affiche">Message 3</p>
                        <p class="message_affiche">Message 4</p>
                        <p class="message_affiche">Message 5</p>
                        <p class="message_affiche">Message 6</p>
                        <p class="message_affiche">Message 7</p>
                        <p class="message_affiche">Message 8</p>
                        <p class="message_affiche">Message 9</p>
                        <p class="message_affiche">Message 10</p>
                        <p class="message_affiche">Message 11</p>
                        <p class="message_affiche">Message 12</p>
                    </section>
                </section>
            </div>
        </div>)
    }
}