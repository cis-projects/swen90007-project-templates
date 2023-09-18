const CONTENT_TYPE_APPLICATION_JSON = 'application/json';

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

  async doCall(path, method, data, signal) {
    const headers = {
      'Content-Type': CONTENT_TYPE_APPLICATION_JSON,
      Accept: CONTENT_TYPE_APPLICATION_JSON,
    };
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
      },
    );
    if (res.status > 299) {
      throw new Error(`expecting success from API for ${method} ${path} but response was status ${res.status}: ${res.statusText}`);
    }
    return res;
  }
}
