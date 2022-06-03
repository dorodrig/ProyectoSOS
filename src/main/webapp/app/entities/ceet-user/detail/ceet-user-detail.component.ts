import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICeetUser } from '../ceet-user.model';

@Component({
  selector: 'ceet-ceet-user-detail',
  templateUrl: './ceet-user-detail.component.html',
})
export class CeetUserDetailComponent implements OnInit {
  ceetUser: ICeetUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ceetUser }) => {
      this.ceetUser = ceetUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
