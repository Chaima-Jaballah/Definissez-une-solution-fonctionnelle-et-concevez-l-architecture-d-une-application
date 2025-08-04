import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Client, IMessage, Message, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
 private stompClient!: Client;
  private sessionId!: string;
  http = inject(HttpClient);
  private onNewSessionCallback?: (session: any) => void;

  connect(sessionId: string, onMessage: (msg: any) => void): void {
    this.sessionId = sessionId;
    const socket = new SockJS('http://localhost:8080/ws');
    this.stompClient = new Client({
      webSocketFactory: () => socket as WebSocket,
      reconnectDelay: 5000,
    });

    this.stompClient.onConnect = () => {

      this.stompClient.subscribe('/topic/messages', (message: IMessage) => {
        const body = JSON.parse(message.body);
        if (body.sessionId === this.sessionId) {
          onMessage(body);
        }
      });

      this.stompClient.subscribe('/topic/sessions', (message: IMessage) => {
        const session = JSON.parse(message.body);
        if (this.onNewSessionCallback) {
          this.onNewSessionCallback(session);
        }
      });
    };

    this.stompClient.activate();
  }

  sendMessage(from: string, role: string, text: string): void {
    const message = {
      from,
      role,
      text,
      sessionId: this.sessionId
    };
    this.stompClient.publish({
      destination: '/app/sendMessage',
      body: JSON.stringify(message)
    });
  }

  getHistory(sessionId: string) {
    return this.http.get<any[]>(`http://localhost:8080/api/chat/history/${sessionId}`);
  }

  getSessions() {
    return this.http.get<any[]>('http://localhost:8080/api/chat/sessions');
  }

  subscribeToNewSessions(onSession: (session: any) => void): void {
    this.onNewSessionCallback = onSession;
  }
}
