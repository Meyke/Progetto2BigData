import { Component, Input } from '@angular/core';
import { AppService } from './app.service';
import { Observable } from 'rxjs';
import { News } from './common/news.model';
import { TouchSequence } from 'selenium-webdriver';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  query: String;
  data: News[];
  
  constructor(private appService: AppService) { }

  go() {
      this.appService.getByTileOrContent(this.query)
      .subscribe(res => {
        for (let news of res){
          news.content = news.content.substring(0,200) + "...";
        }
        this.data = res
      })
      
  }

}
