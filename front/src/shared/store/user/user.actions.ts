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

export class Update {
  static readonly type = '[User] Update user';

  constructor(public user: { name: string; email: string }) {}
}

export class ChangePassword {
  static readonly type = '[User] Change password';

  constructor(
    public actualPassword: string,
    public newPassword: string,
  ) {}
}

export class AddTopic {
  static readonly type = '[User] Add topic';

  constructor(public topicUuid: string) {}
}

export class RemoveTopic {
  static readonly type = '[User] Remove topic';

  constructor(public topicUuid: string) {}
}
