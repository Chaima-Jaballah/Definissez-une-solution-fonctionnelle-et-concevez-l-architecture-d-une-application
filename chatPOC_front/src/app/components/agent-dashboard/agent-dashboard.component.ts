import { Component, inject } from '@angular/core';
import { ChatService } from '../../shared/services/chat.service';

@Component({
  selector: 'app-agent-dashboard',
  standalone: false,
  templateUrl: './agent-dashboard.component.html',
  styleUrl: './agent-dashboard.component.scss'
})
export class AgentDashboardComponent {
 sessions: any[] = [];
  chatService = inject(ChatService);
  constructor() { }

  ngOnInit(): void {
    this.chatService.getSessions()
      .subscribe(data => this.sessions = data);
    this.chatService.connect(
      'agent-dashboard',
      (msg) => { }
    )
    this.chatService.subscribeToNewSessions((newSession) => {
      const index = this.sessions.findIndex(
        s => s.sessionId === newSession.sessionId
      );

      if (index !== -1) {
        this.sessions[index] = {
          ...this.sessions[index],
          ...newSession
        };
        this.sessions = [...this.sessions];
      } else {
        this.sessions = [newSession,...this.sessions];
      }
    });
  }
}
