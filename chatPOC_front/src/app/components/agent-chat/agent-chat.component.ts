import { Component } from '@angular/core';
import { ChatService } from '../../shared/services/chat.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-agent-chat',
  standalone: false,
  templateUrl: './agent-chat.component.html',
  styleUrl: './agent-chat.component.scss'
})
export class AgentChatComponent {
 messages: any[] = [];
  message: string = '';
  sessionId: string = '';
  username: string = 'Agent Your Car Your Way';

  constructor(private chatService: ChatService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.sessionId = params['sessionId'];
      this.chatService.getHistory(this.sessionId).subscribe(history => {
        this.messages = history;
      });
      this.chatService.connect(this.sessionId, (msg) => {
        this.messages.push(msg);
      });
    });
  }

  send(): void {
    if (this.message.trim()) {
      this.chatService.sendMessage(this.username, 'Service client', this.message);
      this.message = '';
    }
  }
}
