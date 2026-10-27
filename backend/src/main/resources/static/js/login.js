const form=document.querySelector('#loginForm'),error=document.querySelector('#loginError');
if(API.token) location.href='/';
form.addEventListener('submit',async e=>{e.preventDefault();error.textContent='';const data=Object.fromEntries(new FormData(form));try{const result=await API.post('/api/auth/login',data);API.setToken(result.accessToken);location.href='/';}catch(err){error.textContent=err.message||'Unable to sign in. Check your credentials.';}});
