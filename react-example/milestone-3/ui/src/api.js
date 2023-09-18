const CONTENT_TYPE_APPLICATION_JSON = 'application/json';
const STORAGE_ITEM_ACCESS_TOKEN = 'accessToken';
const STORAGE_ITEM_TOKEN_TYPE = 'tokenType';

const setTokenInStorage = (token) => {
  localStorage.setItem(STORAGE_ITEM_ACCESS_TOKEN, token.accessToken);
  localStorage.setItem(STORAGE_ITEM_TOKEN_TYPE, token.type);
};

export default class Api {
  constructor(baseUrl) {
    this.baseUrl = baseUrl;
  }

  async getVerdict(signal) {
    const res = await this.doCall('/verdict', 'GET', null, signal);
    return res.json();
  }

  async submitVote(name, email, supporting, signal) {
    const res = await this.doCall('/vote', 'POST', { email, name, supporting }, signal);
    return res.json();
  }

  async getVotes(offset, limit, signal) {
    const res = await this.doCall(`/manage/vote?${new URLSearchParams({ offset, limit })}`, 'GET', null, signal);
    return res.json();
  }

  async updateVote(vote, signal) {
    const res = await this.doCall(`/manage/vote/${vote.id}`, 'PUT', vote, signal);
    return res.json();
  }

  async login(username, password, signal) {
    const res = await this.doCall('/auth/token', 'POST', { username, password }, signal);
    const token = await res.json();
    setTokenInStorage(token);
    return token;
  }

  async logout(username, signal) {
    await this.doCall('/auth/logout', 'POST', { username }, signal);
    localStorage.removeItem(STORAGE_ITEM_ACCESS_TOKEN);
    localStorage.removeItem(STORAGE_ITEM_TOKEN_TYPE);
  }

  async refreshToken(accessToken, signal) {
    const path = '/auth/token';
    const res = await fetch(
      `${this.baseUrl}${path}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': CONTENT_TYPE_APPLICATION_JSON,
          Accept: CONTENT_TYPE_APPLICATION_JSON,
        },
        body: JSON.stringify({
          accessToken,
        }),
        signal,
        credentials: 'include',
      },
    );
    if (res.status > 299) {
      throw new Error(`expecting success from API for PUT ${path} but response was status ${res.status}: ${res.statusText}`);
    }
    const token = await res.json();
    setTokenInStorage(token);
    return token;
  }

  async doCall(path, method, data, signal) {
    const headers = {
      'Content-Type': CONTENT_TYPE_APPLICATION_JSON,
      Accept: CONTENT_TYPE_APPLICATION_JSON,
    };
    const accessToken = localStorage.getItem(STORAGE_ITEM_ACCESS_TOKEN);
    if (accessToken) {
      headers.Authorization = `${localStorage.getItem(STORAGE_ITEM_TOKEN_TYPE)} ${accessToken}`;
    }
    let body;
    if (data) {
      body = JSON.stringify(data);
    }
    const res = await fetch(
      `${this.baseUrl}${path}`,
      {
        method,
        headers,
        body,
        signal,
        credentials: 'include',
      },
    );
    if (res.status === 401 && accessToken) {
      await this.refreshToken(accessToken, signal);
      return this.doCall(path, method, data, signal);
    }
    if (res.status > 299) {
      throw new Error(`expecting success from API for ${method} ${path} but response was status ${res.status}: ${res.statusText}`);
    }
    return res;
  }
}
