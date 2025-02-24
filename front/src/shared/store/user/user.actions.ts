/* eslint-disable no-unused-vars */

export class Me {
  static readonly type = '[User] ask who am I';
}

export class Refresh {
  static readonly type = '[User] refresh accessToken';
}

export class Logout {
  static readonly type = '[User] Log out';
}

export class Login {
  static readonly type = '[User] Log in';

  constructor(
    public username: string,
    public password: string,
  ) {}
}
