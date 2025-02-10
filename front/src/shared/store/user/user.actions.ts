export class Me {
  static readonly type = '[User] init access token right after SSO redirection';
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
