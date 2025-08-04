import { Component } from '@angular/core';
import { ChatService } from '../../shared/services/chat.service';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-client-chat',
  standalone: false,
  templateUrl: './client-chat.component.html',
  styleUrl: './client-chat.component.scss'
})
export class ClientChatComponent {
 messages: any[] = [];
  message: string = '';
  sessionId: string;
  username: string = '';

  constructor(private chatService: ChatService) {
    this.sessionId = uuidv4(); // Crée une session unique côté client
  }

  ngOnInit(): void {
    this.chatService.connect(this.sessionId, (msg) => {
      this.messages.push(msg);
    });
  }

  send(): void {
    if (this.message.trim()) {
      this.chatService.sendMessage(this.username, 'client', this.message);
      this.message = '';
    }
  }
}
