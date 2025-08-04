import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientChatComponent } from './components/client-chat/client-chat.component';
import { AgentChatComponent } from './components/agent-chat/agent-chat.component';
import { AgentDashboardComponent } from './components/agent-dashboard/agent-dashboard.component';

const routes: Routes = [
  { path: 'client', component: ClientChatComponent },
  { path: 'agent', component: AgentChatComponent },
  { path: 'dashboard', component: AgentDashboardComponent },
  { path: '**', redirectTo: 'client' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
