const API = {
  tokenKey: 'smartstock_token',
  get token(){ return sessionStorage.getItem(this.tokenKey); },
  setToken(v){ if(v) sessionStorage.setItem(this.tokenKey,v); else sessionStorage.removeItem(this.tokenKey); },
  async request(path, options={}) {
    const headers = {'Content-Type':'application/json', ...(options.headers||{})};
    if(this.token) headers.Authorization = `Bearer ${this.token}`;
    const response = await fetch(path, {...options, headers});
    if(response.status===401 && !path.endsWith('/api/auth/login')) { this.setToken(null); if(!location.pathname.endsWith('login.html')) location.href='/login.html'; }
    const type=response.headers.get('content-type')||'';
    const body=type.includes('application/json') ? await response.json().catch(()=>null) : await response.text().catch(()=> '');
    if(!response.ok) throw new Error(body?.message || body?.error || (typeof body==='string'&&body) || `Request failed (${response.status})`);
    return body;
  },
  get(path){return this.request(path);},
  post(path,data){return this.request(path,{method:'POST',body:JSON.stringify(data)});},
  put(path,data){return this.request(path,{method:'PUT',body:JSON.stringify(data)});},
  delete(path){return this.request(path,{method:'DELETE'});}
};
