import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  prompt: string = '';
  response: string = '';
  loading: boolean = false;

  constructor(private http: HttpClient) {}

  onSubmit() {
    this.loading = true;
    const safePrompt = encodeURIComponent(this.prompt);
    
    this.http.get(`http://localhost:8080/api/ollama/angular/${safePrompt}`, {
      responseType: 'text'
    })
    .subscribe({
      next: (data) => this.response = data,
      error: () => this.response = 'Error connecting to localhost Ollama API.',
      complete: () => this.loading = false
    });
  }

  // Clear function to reset variables
  onClear() {
    this.prompt = '';
    this.response = '';
  }
}
