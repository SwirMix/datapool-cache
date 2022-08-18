(function(){"use strict";var t={7718:function(t,e,a){var o=a(144),r=a(998),s=a(5174),i=a(3423),n=a(757),l=a(6190),c=a(9582),d=a(4886),_=a(9256),u=a(9223),p=a(5125),h=a(4324),m=a(3201),v=a(6098),E=a(3059),g=a(4528),C=a(2933),f=a(9481),R=a(3687),T=a(7808),Z=a(1783),S=a(7953),b=function(){var t=this,e=t._self._c;return e(r.Z,[e(s.Z,{attrs:{app:"",dark:""}},[e("h2",[t._v("HttpDatapool")]),e(R.Z),e("DeleteProjectDialog",{staticClass:"mr-3 ma-3"}),e(f.Z,{staticClass:"mt-7 mr-4",attrs:{items:this.GET_PROJECTS(),label:"Project"},scopedSlots:t._u([{key:"item",fn:function({item:e}){return[t._v(" "+t._s(e.name)+" ["+t._s(e.id)+"] ")]}},{key:"selection",fn:function({item:e}){return[t._v(" "+t._s(e.name)+" ["+t._s(e.id)+"] ")]}}]),model:{value:t.project,callback:function(e){t.project=e},expression:"project"}}),e(g.Z,{attrs:{bottom:"","min-width":"200px",rounded:"","offset-y":""},scopedSlots:t._u([{key:"activator",fn:function({on:a}){return[e(l.Z,t._g({attrs:{icon:"","x-large":""}},a),[e(i.Z,{attrs:{color:"brown",size:"48"}},[e("span",{staticClass:"white--text text-h5"},[t._v(t._s(t.user.initials))])])],1)]}}])},[e(c.Z,[e(v.km,{staticClass:"justify-center"},[e("div",{staticClass:"mx-auto text-center"},[e(i.Z,{attrs:{color:"brown"}},[e("span",{staticClass:"white--text text-h5"},[t._v(t._s(t.user.initials))])]),e("h3",[t._v(t._s(t.user.fullName))]),e("p",{staticClass:"text-caption mt-1"},[t._v(" "+t._s(t.user.email)+" ")]),e(u.Z,{staticClass:"my-3"}),e("RemoteTokenDialog"),e(u.Z,{staticClass:"my-3"}),e("CreateProjectDialog"),e(u.Z,{staticClass:"my-3"}),e(l.Z,{attrs:{depressed:"",rounded:"",text:""},on:{click:function(e){return t.logout()}}},[t._v(" Logout ")])],1)])],1)],1)],1),e(E.Z,[e(C.Z,{attrs:{value:this.overlay}},[e(_.Z,{attrs:{fluid:"","fill-height":""}},[e(m.Z,{attrs:{"align-center":"","justify-center":""}},[e(c.Z,{staticStyle:{width:"30rem"}},[e(Z.Z,{attrs:{dark:"",color:"primary"}},[e(S.qW,[t._v("Login form")])],1),e(d.ZB,[e(p.Z,[e(T.Z,{attrs:{"prepend-icon":"mdi-account",name:"login",label:"Login",type:"text"},model:{value:t.auth_data.login,callback:function(e){t.$set(t.auth_data,"login",e)},expression:"auth_data.login"}}),e(T.Z,{attrs:{id:"password","prepend-icon":"mdi-lock",name:"password",label:"Password",type:"password"},model:{value:t.auth_data.pass,callback:function(e){t.$set(t.auth_data,"pass",e)},expression:"auth_data.pass"}})],1)],1),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"primary"},on:{click:function(e){return t.auth()}}},[t._v("Login")])],1)],1)],1)],1)],1),e("router-view")],1),e(n.Z,{model:{value:t.value,callback:function(e){t.value=e},expression:"value"}},[e(l.Z,{staticClass:"my-2",attrs:{value:"Http caches",text:"",to:"/"}},[e("span",[t._v("Http caches")]),e(h.Z,[t._v("mdi-database")])],1),e(l.Z,{staticClass:"my-2",attrs:{value:"CSV storage",text:"",to:"/storage"}},[e("span",[t._v("CSV storage")]),e(h.Z,[t._v("mdi-nas")])],1),e(l.Z,{staticClass:"my-2",attrs:{value:"Static parameters",text:"",to:"/params"}},[e("span",[t._v("Static parameters")]),e(h.Z,[t._v("mdi-star-settings")])],1),e(l.Z,{staticClass:"my-2",attrs:{value:"Datapool cluster",text:"",to:"/cluster"}},[e("span",[t._v("Datapool cluster")]),e(h.Z,[t._v("mdi-webpack")])],1)],1)],1)},y=[],k=a(9669),x=a.n(k),j={server_endpoint:"",api:{post_auth:"api/v1/account/auth",remote_token_auth:"api/v1/account/remote/auth",get_projects:"api/v1/projects/",get_caches:"api/v1/caches",get_storage:"api/v1/storage",post_reload_cache:"api/v1/storage/reload",post_reload_internal_cache:"api/v1/datapool/cache",delete_cache:"api/v1/datapool/cache",delete_file:"api/v1/storage",post_upload_file:"api/v1/storage/upload",post_import_cache_to_file:"api/v1/storage/import",post_cache_from_jdbc:"api/v1/datapool/cache/create",get_cluster:"api/v1/cluster",post_create_project:"api/v1/projects/create",delete_project:"api/v1/projects/delete",get_static_caches:"api/v1/static/caches",patch_static_cache:"api/v1/datapool/static/update",delete_static_cache:"api/v1/datapool/static/single",create_static_cache:"api/v1/static/create"},create_cache_static(t){var e={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};return x().post(this.server_endpoint+this.api.create_static_cache,t,e)},delete_cache_static(t){var e={headers:{"content-type":"application/json","Access-Control-Allow-Origin":"*",token:localStorage.getItem("http.datapool.token")},method:"DELETE"};return x()(this.server_endpoint+this.api.delete_static_cache+"?cacheUuid="+t.cacheUUID+"&projectId="+t.projectId,e)},patch_cache_static(t){let e={value:{name:t.name,projectId:t.projectId,parameters:t.parameters},key:{project:t.projectId,cacheUUID:t.cacheUUID}};var a={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"PATCH"};return x().patch(this.server_endpoint+this.api.patch_static_cache,e,a)},post_create_project(t,e){var a={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};let o={name:t,description:e};return x().post(this.server_endpoint+this.api.post_create_project,o,a)},auth(t){console.log(this.type);var e={headers:{"content-type":"application/json"},method:"POST"};return x().post(this.server_endpoint+this.api.post_auth,t,e)},remote_auth(){console.log(this.type);var t={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};return x().get(this.server_endpoint+this.api.remote_token_auth,t)},get_cluster_info(){console.log(this.type);var t={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"GET"};return x().get(this.server_endpoint+this.api.get_cluster,t)},get_static_data_caches(t){console.log(this.type);var e={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"GET"};return x().get(this.server_endpoint+this.api.get_static_caches+"?projectId="+t,e)},get_projects(){var t={headers:{"content-type":"application/json","Access-Control-Allow-Origin":"*",token:localStorage.getItem("http.datapool.token")},method:"GET"};return x()(this.server_endpoint+this.api.get_projects,t)},get_caches(t){var e={headers:{"content-type":"application/json","Access-Control-Allow-Origin":"*",token:localStorage.getItem("http.datapool.token")},method:"GET"};return x()(this.server_endpoint+this.api.get_caches+"?project="+t,e)},get_storage(t){var e={headers:{"content-type":"application/json","Access-Control-Allow-Origin":"*",token:localStorage.getItem("http.datapool.token")},method:"GET"};return x()(this.server_endpoint+this.api.get_storage+"?storage_id="+t,e)},delete_cache(t){var e={headers:{"content-type":"application/json","Access-Control-Allow-Origin":"*",token:localStorage.getItem("http.datapool.token")},method:"DELETE"};return x()(this.server_endpoint+this.api.delete_cache+"/"+t,e)},delete_project_request(t){var e={headers:{"content-type":"application/json","Access-Control-Allow-Origin":"*",token:localStorage.getItem("http.datapool.token")},method:"DELETE"};return x()(this.server_endpoint+this.api.delete_project+"/"+t,e)},delete_file(t,e){var a={headers:{"content-type":"application/json","Access-Control-Allow-Origin":"*",token:localStorage.getItem("http.datapool.token")},method:"DELETE"};return x()(this.server_endpoint+this.api.delete_file+"/"+t+"/"+e,a)},import_cache_request(t,e){console.log(this.type);var a={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};let o={cacheName:t.cacheName,fileName:e,override:!0,project:t.baseProject};return x().post(this.server_endpoint+this.api.post_import_cache_to_file,o,a)},post_reload_cache(t,e){console.log(this.type);var a={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};let o={};return x().post(this.server_endpoint+this.api.post_reload_cache+"?fileName="+e+"&project="+t,o,a)},post_reload_all(t){console.log(this.type);var e={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};let a={};return x().post(this.server_endpoint+this.api.post_reload_cache+"?project="+t,a,e)},post_reload_internal_cache(t){console.log(this.type);var e={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};let a={};return x().post(this.server_endpoint+this.api.post_reload_internal_cache+"/"+t,a,e)},post_upload_file(t,e){console.log(this.file);var a={headers:{"content-type":"multipart/form-data",token:localStorage.getItem("http.datapool.token")},method:"POST"};return x().post(this.server_endpoint+this.api.post_upload_file+"?project="+e,t,a)},post_jdbc_cache(t){console.log(t);var e={headers:{"content-type":"application/json",token:localStorage.getItem("http.datapool.token")},method:"POST"};return x().post(this.server_endpoint+this.api.post_cache_from_jdbc,t,e)}},P=j,O=a(9228),N=a(6072),w=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(l.Z,t._g(t._b({attrs:{depressed:"",rounded:"",text:""}},"v-btn",o,!1),a),[t._v(" Generate remote token ")])]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Remote token for HttpPool ")]),e(d.ZB,[e(N.Z,{attrs:{solo:"",name:"input-7-4",label:"remote token"},model:{value:t.token,callback:function(e){t.token=e},expression:"token"}})],1),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){return t.generate()}}},[t._v(" generate ")])],1)],1)],1)],1)},J=[],U={data(){return{dialog:!1,token:""}},methods:{generate(){P.remote_auth().then((t=>{this.token=t.data.token}))}}},H=U,I=a(1001),A=(0,I.Z)(H,w,J,!1,null,null,null),D=A.exports,G=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(l.Z,t._g(t._b({attrs:{depressed:"",rounded:"",text:""}},"v-btn",o,!1),a),[t._v(" Create new project space ")])]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Create Project ")]),e(d.ZB,[e(T.Z,{attrs:{label:"Project name"},model:{value:t.name,callback:function(e){t.name=e},expression:"name"}}),e(N.Z,{attrs:{solo:"",name:"input-7-4",label:"Project description"},model:{value:t.description,callback:function(e){t.description=e},expression:"description"}})],1),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"success",text:""},on:{click:function(e){return t.create_project()}}},[t._v(" Create ")])],1)],1)],1)],1)},F=[],q=a(629),B={data(){return{dialog:!1,name:"",description:""}},methods:{...(0,q.nv)(["REFRESH_CACHES","REFRESH_PROJECTS"]),create_project(){P.post_create_project(this.name,this.description).then((t=>{console.log(t.data),this.REFRESH_PROJECTS(),this.dialog=!1}))}}},L=B,$=(0,I.Z)(L,G,F,!1,null,null,null),z=$.exports,Q=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(h.Z,t._g(t._b({attrs:{medium:""}},"v-icon",o,!1),a),[t._v(" mdi-delete ")])]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Confirm deletion ")]),e(d.ZB,[t._v(" Are you sure you want to delete cache - "+t._s(this.item.cacheName)+" ")]),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" Cancel ")]),e(l.Z,{attrs:{color:"error",text:""},on:{click:function(e){return t.delete_action()}}},[t._v(" Confirm ")])],1)],1)],1)],1)},M=[],V={props:{item:{type:Object,default(){return{}}}},data(){return{dialog:!1}},methods:{...(0,q.Se)(["GET_CURRENT_PROJECT"]),...(0,q.nv)(["REFRESH_CACHES","REFRESH_CURRENT_PROJECT"]),delete_action(){console.log(this.item),P.delete_project_request(this.GET_CURRENT_PROJECT().id),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT()),this.dialog=!1}}},K=V,W=(0,I.Z)(K,Q,M,!1,null,null,null),Y=W.exports,X={name:"App",components:{RemoteTokenDialog:D,CreateProjectDialog:z,DeleteProjectDialog:Y},data:()=>({absolute:!0,overlay:!0,auth_data:{login:"",pass:""},user:{initials:"you",fullName:"",email:""},project:""}),watch:{project(){this.REFRESH_CURRENT_PROJECT(this.project)}},computed:{},methods:{...(0,q.Se)(["GET_OVERLAY","GET_PROJECTS"]),...(0,q.nv)(["REFRESH_PROJECTS","REFRESH_CURRENT_PROJECT"]),...(0,q.OI)(["SET_CURRENT_PROJECT"]),auth(){P.auth(this.auth_data).then((t=>{localStorage.setItem("http.datapool.token",t.data.token),localStorage.setItem("http.datapool.login",t.data.login),localStorage.setItem("http.datapool.email",t.data.email),console.log(t.data),this.overlay=!1,this.REFRESH_PROJECTS(),this.$router.push({path:"/datapools"})}))},logout(){localStorage.removeItem("http.datapool.token"),this.overlay=!0},init(){localStorage.getItem("http.datapool.token")?this.overlay=!1:this.overlay=!0,this.REFRESH_PROJECTS(),this.user.fullName=localStorage.getItem("http.datapool.login"),this.user.email=localStorage.getItem("http.datapool.email")}},mounted(){this.init()},created(){this.init()}},tt=X,et=(0,I.Z)(tt,b,y,!1,null,null,null),at=et.exports,ot=a(1705);o.ZP.use(ot.Z);var rt=new ot.Z({theme:{dark:!0}}),st=a(1641),it=a(1713),nt=function(){var t=this,e=t._self._c;return e(_.Z,{attrs:{fluid:""}},[e(c.Z,{staticClass:"my-4",attrs:{flat:""}},[e("v-card-content",{staticClass:"d-flex flex-row-reverse"},[e("CreateCacheDialog",{staticClass:"mx-4 my-3"}),e(l.Z,{staticClass:"mx-4 my-3",attrs:{fab:"",dark:"",color:"warning",small:""},on:{click:function(e){return t.reload_all()}}},[e(h.Z,{attrs:{dark:""}},[t._v(" mdi-reload ")])],1)],1)],1),e(T.Z,{staticClass:"my-2",attrs:{"append-icon":"mdi-magnify",label:"Search","single-line":"","hide-details":""},model:{value:t.search,callback:function(e){t.search=e},expression:"search"}}),e(st.Z,{staticClass:"elevation-1",attrs:{headers:t.headers,items:this.$store.state.caches,"items-per-page":10,search:t.search},scopedSlots:t._u([{key:"item.actions",fn:function({item:t}){return[e(it.Z,[e("ReloadConfirmDialog",{staticClass:"ma-2",attrs:{item:t}}),e("DeleteDialog",{staticClass:"ma-2",attrs:{item:t}}),e("CacheInfoDialog",{staticClass:"ma-2",attrs:{item:t}})],1)]}},{key:"no-data",fn:function(){return[e(l.Z,{attrs:{color:"primary"}},[t._v(" Reset ")])]},proxy:!0}],null,!0)})],1)},lt=[],ct=a(255),dt=a(2348),_t=a(400),ut=a(1214),pt=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(h.Z,t._g(t._b({attrs:{medium:""}},"v-icon",o,!1),a),[t._v(" mdi-information ")])]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Cache info ")]),e(d.ZB,[e("h3",[e("b",[t._v("CacheName:")]),t._v(" "+t._s(t.item.cacheName))]),e("h3",[e("b",[t._v("Type:")]),t._v(" "+t._s(t.item.type))]),e("h3",[e("b",[t._v("Status:")]),t._v(" "+t._s(t.item.status))]),e("h3",[e("b",[t._v("Size:")]),t._v(" "+t._s(t.item.rowCount))]),e("h3",[e("b",[t._v("Columns:")]),t._v(" "+t._s(t.item.columns))]),e("h3",[e("b",[t._v("Connection:")]),t._v(" "+t._s(t.item.connectionProperties))]),e("h3",[e("b",[t._v("Consumers:")]),t._v(" "+t._s(t.item.consumers))]),e("h3",[e("b",[t._v("Project:")]),t._v(" "+t._s(t.item.baseProject))]),e(T.Z,{attrs:{label:"Export file"},model:{value:t.import_file,callback:function(e){t.import_file=e},expression:"import_file"}}),e(ut.Z,[e(ct.Z,[e(_t.Z,[t._v(" Query ")]),e(dt.Z,[e(_.Z,{attrs:{fluid:""}},[e(N.Z,{attrs:{name:"input-7-1",filled:"",label:"Datasource query","auto-grow":"",disabled:"",value:t.item.query}})],1)],1)],1)],1)],1),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"success",text:""},on:{click:function(e){return t.import_request()}}},[t._v(" import cache to file ")]),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" OK ")])],1)],1)],1)],1)},ht=[],mt={props:{item:{type:Object,default(){return{}}}},data(){return{dialog:!1,import_file:""}},methods:{...(0,q.nv)(["REFRESH_CACHES"]),import_request(){P.import_cache_request(this.item,this.import_file),this.REFRESH_CACHES(this.item.baseProject)}}},vt=mt,Et=(0,I.Z)(vt,pt,ht,!1,null,null,null),gt=Et.exports,Ct=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(h.Z,t._g(t._b({attrs:{medium:""}},"v-icon",o,!1),a),[t._v(" mdi-delete ")])]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Confirm deletion ")]),e(d.ZB,[t._v(" Are you sure you want to delete cache - "+t._s(this.item.cacheName)+" ")]),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" Cancel ")]),e(l.Z,{attrs:{color:"error",text:""},on:{click:function(e){return t.delete_action()}}},[t._v(" Confirm ")])],1)],1)],1)],1)},ft=[],Rt={props:{item:{type:Object,default(){return{}}}},data(){return{dialog:!1}},methods:{...(0,q.nv)(["REFRESH_CACHES"]),delete_action(){console.log(this.item),P.delete_cache(this.item.cacheName),this.REFRESH_CACHES(this.item.baseProject),this.dialog=!1}}},Tt=Rt,Zt=(0,I.Z)(Tt,Ct,ft,!1,null,null,null),St=Zt.exports,bt=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(h.Z,t._g(t._b({attrs:{medium:""}},"v-icon",o,!1),a),[t._v(" mdi-reload ")])]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Confirm reload cache ")]),e(d.ZB,[e("h3",[e("b",[t._v("CacheName:")]),t._v(" "+t._s(t.item.cacheName))]),e("h3",[e("b",[t._v("Type:")]),t._v(" "+t._s(t.item.type))]),e("h3",[e("b",[t._v("Status:")]),t._v(" "+t._s(t.item.status))]),e("h3",[e("b",[t._v("Size:")]),t._v(" "+t._s(t.item.rowCount))]),e("h3",[e("b",[t._v("Columns:")]),t._v(" "+t._s(t.item.columns))]),e("h3",[e("b",[t._v("Connection:")]),t._v(" "+t._s(t.item.connectionProperties))]),e("h3",[e("b",[t._v("Consumers:")]),t._v(" "+t._s(t.item.consumers))]),e("h3",[e("b",[t._v("Project:")]),t._v(" "+t._s(t.item.baseProject))]),e(ut.Z,[e(ct.Z,[e(_t.Z,[t._v(" Query ")]),e(dt.Z,[e(_.Z,{attrs:{fluid:""}},[e(N.Z,{attrs:{name:"input-7-1",filled:"",label:"Datasource query","auto-grow":"",disabled:"",value:t.item.query}})],1)],1)],1)],1)],1),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" Cancel ")]),e(l.Z,{attrs:{color:"warning",text:""},on:{click:function(e){return t.reload_request()}}},[t._v(" Confirm ")])],1)],1)],1)],1)},yt=[],kt={props:{item:{type:Object,default(){return{}}}},data(){return{dialog:!1}},methods:{...(0,q.Se)(["GET_CURRENT_PROJECT"]),...(0,q.nv)(["REFRESH_CURRENT_PROJECT"]),reload_request(){P.post_reload_internal_cache(this.item.cacheName).then((t=>{console.log(t.data),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT()),this.dialog=!1}))}}},xt=kt,jt=(0,I.Z)(xt,bt,yt,!1,null,null,null),Pt=jt.exports,Ot=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(l.Z,t._g(t._b({attrs:{fab:"",dark:"",color:"indigo",small:""}},"v-btn",o,!1),a),[e(h.Z,{attrs:{dark:""}},[t._v(" mdi-plus ")])],1)]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Add cache from JDBC ")]),e(d.ZB,[e(T.Z,{attrs:{label:"Query",required:""},model:{value:t.body.query,callback:function(e){t.$set(t.body,"query",e)},expression:"body.query"}}),e(f.Z,{attrs:{items:t.types,label:"Type",required:""},model:{value:t.body.type,callback:function(e){t.$set(t.body,"type",e)},expression:"body.type"}}),e(T.Z,{attrs:{label:"CacheName",required:""},model:{value:t.body.cacheName,callback:function(e){t.$set(t.body,"cacheName",e)},expression:"body.cacheName"}}),e(T.Z,{attrs:{label:"JDBC URL",required:""},model:{value:t.body.connectionProperties.url,callback:function(e){t.$set(t.body.connectionProperties,"url",e)},expression:"body.connectionProperties.url"}}),e(T.Z,{attrs:{label:"login",required:""},model:{value:t.body.connectionProperties.login,callback:function(e){t.$set(t.body.connectionProperties,"login",e)},expression:"body.connectionProperties.login"}}),e(T.Z,{attrs:{label:"password",required:""},model:{value:t.body.connectionProperties.password,callback:function(e){t.$set(t.body.connectionProperties,"password",e)},expression:"body.connectionProperties.password"}})],1),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"warning",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" Cancel ")]),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){return t.create_cache()}}},[t._v(" Confirm ")])],1)],1)],1)],1)},Nt=[],wt={params:{item:Object},data(){return{dialog:!1,types:["POSTGRESQL"],body:{query:"select * from bookings.aircrafts_data",connectionProperties:{url:"jdbc:postgresql://localhost:5432/demo?currentSchema=bookings",login:"perfcona",password:"perfcona"},type:"POSTGRESQL",cacheName:"aircraft",projectId:"20c794f4-46d2-46a1-af19-5efbd1addf5d"}}},methods:{...(0,q.Se)(["GET_CURRENT_PROJECT"]),...(0,q.nv)(["REFRESH_CURRENT_PROJECT"]),create_cache(){this.body.projectId=this.GET_CURRENT_PROJECT().id,P.post_jdbc_cache(this.body).then((t=>{console.log(t.data),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT()),this.dialog=!1}))}}},Jt=wt,Ut=(0,I.Z)(Jt,Ot,Nt,!1,null,null,null),Ht=Ut.exports,It={components:{CacheInfoDialog:gt,DeleteDialog:St,ReloadConfirmDialog:Pt,CreateCacheDialog:Ht},data(){return{search:"",headers:[{text:"CacheName",align:"start",sortable:!1,value:"cacheName"},{text:"Type",value:"type"},{text:"project",value:"baseProject"},{text:"rowCount",value:"rowCount"},{text:"Columns",value:"columns"},{text:"Status",value:"status"},{text:"Actions",value:"actions"}]}},methods:{...(0,q.Se)(["GET_CURRENT_PROJECT","GET_CACHES"]),...(0,q.nv)(["REFRESH_CACHES","REFRESH_CURRENT_PROJECT"]),get_caches(){return this.REFRESH_CACHES(this.GET_CURRENT_PROJECT().id),this.GET_CACHES()},reload_all(){P.post_reload_all(this.GET_CURRENT_PROJECT().id).then((t=>{console.log(t),this.REFRESH_CACHES(this.GET_CURRENT_PROJECT().id)}))}},created(){this.get_caches()}},At=It,Dt=(0,I.Z)(At,nt,lt,!1,null,null,null),Gt=Dt.exports,Ft=function(){var t=this,e=t._self._c;return e(_.Z,{attrs:{fluid:""}},[e(st.Z,{staticClass:"elevation-1",attrs:{headers:t.headers,items:t.cluster,"items-per-page":20,search:t.search},scopedSlots:t._u([{key:"cluster.NodeId",fn:function({cluster:e}){return[t._v(" "+t._s(e.attributes)+" ")]}}],null,!0)})],1)},qt=[],Bt={data(){return{search:"",headers:[{text:"NodeId",align:"start",sortable:!1,value:"nodeId"},{text:"instanceName",value:"instanceName"},{text:"port",value:"port"},{text:"addresses",value:"ips"},{text:"cpu",value:"cores"}],cluster:[]}},methods:{},created(){P.get_cluster_info().then((t=>{this.cluster=t.data}))},mounted(){P.get_cluster_info().then((t=>{this.cluster=t.data}))}},Lt=Bt,$t=(0,I.Z)(Lt,Ft,qt,!1,null,null,null),zt=$t.exports,Qt=a(1625),Mt=function(){var t=this,e=t._self._c;return e(_.Z,{attrs:{fluid:""}},[e(c.Z,{staticClass:"my-4",attrs:{flat:""}},[e("v-card-content",{staticClass:"d-flex flex-row-reverse"},[e(l.Z,{staticClass:"ma-4",attrs:{fab:"",dark:"",color:"indigo"},on:{click:function(e){return t.upload_file_request()}}},[e(h.Z,{attrs:{dark:""}},[t._v(" mdi-plus ")])],1),e(Qt.Z,{staticClass:"ma-4",attrs:{"truncate-length":"15"},model:{value:t.upload_file,callback:function(e){t.upload_file=e},expression:"upload_file"}})],1)],1),e(T.Z,{staticClass:"my-2",attrs:{"append-icon":"mdi-magnify",label:"Search","single-line":"","hide-details":""},model:{value:t.search,callback:function(e){t.search=e},expression:"search"}}),e(st.Z,{staticClass:"elevation-1 text-right",attrs:{headers:t.headers,items:this.GET_STORAGE(),"items-per-page":10,search:t.search},scopedSlots:t._u([{key:"item.actions",fn:function({item:a}){return[e("div",{staticClass:"d-flex justify-end"},[e("StorageImportDialog",{staticClass:"mr-4",attrs:{item:a}}),e("DeleteStorageFileDialog",{attrs:{item:a}}),e(l.Z,{staticClass:"ma-2 white--text",attrs:{color:"white",outlined:"",href:t.download_url(a)}},[t._v(" Download "),e(h.Z,{attrs:{right:"",dark:""}},[t._v(" mdi-download-circle ")])],1)],1)]}}],null,!0)})],1)},Vt=[],Kt=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(l.Z,t._g(t._b({staticClass:"ma-2 white--text",attrs:{color:"error",outlined:""}},"v-btn",o,!1),a),[t._v(" Delete "),e(h.Z,{attrs:{right:"",dark:""}},[t._v(" mdi-delete ")])],1)]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Confirm deletion ")]),e(d.ZB,[t._v(" Are you sure you want to delete? ")]),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" Cancel ")]),e(l.Z,{attrs:{color:"error",text:""},on:{click:function(e){return t.delete_file(t.item)}}},[t._v(" Confirm ")])],1)],1)],1)],1)},Wt=[],Yt={props:{item:{type:Object,default(){return{}}}},data(){return{dialog:!1}},methods:{...(0,q.nv)(["REFRESH_CURRENT_PROJECT"]),...(0,q.Se)(["GET_CURRENT_PROJECT"]),delete_file(t){P.delete_file(t.project_id,t.name),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT()),this.dialog=!1}}},Xt=Yt,te=(0,I.Z)(Xt,Kt,Wt,!1,null,null,null),ee=te.exports,ae=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"80rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(l.Z,t._g(t._b({staticClass:"ma-2 white--text",attrs:{color:"primary",outlined:""}},"v-btn",o,!1),a),[t._v(" Import to cache "),e(h.Z,{attrs:{right:"",dark:""}},[t._v(" mdi-database-import ")])],1)]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Confirm import ")]),e(d.ZB,[t._v(" import file [ "+t._s(t.item.name)+"] to cache ")]),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"primary",text:""},on:{click:function(e){t.dialog=!1}}},[t._v(" Cancel ")]),e(l.Z,{attrs:{color:"error",text:""},on:{click:function(e){return t.reload_request()}}},[t._v(" Confirm ")])],1)],1)],1)],1)},oe=[],re={props:{item:{type:Object,default(){return{}}}},data(){return{dialog:!1}},methods:{reload_request(){P.post_reload_cache(this.item.project_id,this.item.name).then((t=>{console.log(t.data),this.dialog=!1}))}}},se=re,ie=(0,I.Z)(se,ae,oe,!1,null,null,null),ne=ie.exports,le={components:{DeleteStorageFileDialog:ee,StorageImportDialog:ne},data(){return{upload_file:{},search:"",headers:[{text:"fileName",align:"start",sortable:!1,value:"name"},{text:"size",value:"size"},{text:"",value:"actions"}]}},methods:{...(0,q.Se)(["GET_STORAGE","GET_CURRENT_PROJECT"]),...(0,q.nv)(["REFRESH_CURRENT_PROJECT"]),upload_file_request(){console.log(this.upload_file);let t=new FormData;t.append("file",this.upload_file),P.post_upload_file(t,this.GET_CURRENT_PROJECT().id).then((t=>{console.log(t.data),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())}))},download_url(t){return P.server_endpoint+t.download}}},ce=le,de=(0,I.Z)(ce,Mt,Vt,!1,null,null,null),_e=de.exports,ue=a(266),pe=function(){var t=this,e=t._self._c;return e(_.Z,{attrs:{fluid:""}},[e(c.Z,{staticClass:"my-3"},[e(d.ZB,{staticClass:"text-right"},[e("CreateStaticCacheDialog")],1)],1),e(T.Z,{staticClass:"my-2",attrs:{"append-icon":"mdi-magnify",label:"Search","single-line":"","hide-details":""},model:{value:t.search,callback:function(e){t.search=e},expression:"search"}}),e(st.Z,{staticClass:"elevation-1",attrs:{headers:t.headers,items:this.GET_STATIC_CACHES(),"items-per-page":10,search:t.search},scopedSlots:t._u([{key:"item.actions",fn:function({item:t}){return[e(it.Z,[e(ue.Z,[e("StaticInfoDialog",{staticClass:"ma-3",attrs:{param_item:t}})],1)],1)]}}],null,!0)})],1)},he=[],me=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-center"},[e(O.Z,{attrs:{width:"90rem",height:"10rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(h.Z,t._g(t._b({attrs:{medium:""}},"v-icon",o,!1),a),[t._v(" mdi-information ")])]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" "+t._s(t.param_item.name)+" ["+t._s(t.param_item.cacheUUID)+"] ")]),e(d.ZB,[e("h3",[e("b",[t._v("lastUpdate:")]),t._v(" "+t._s(t.param_item.lastUpdate))]),e("h3",[e("b",[t._v("name:")]),t._v(" "+t._s(t.param_item.name))]),e("h3",[e("b",[t._v("cacheUUID:")]),t._v(" "+t._s(t.param_item.cacheUUID))]),e("h3",[e("b",[t._v("projectId:")]),t._v(" "+t._s(t.param_item.projectId))]),e(c.Z,[e(d.ZB,[e(N.Z,{attrs:{outlined:"",name:"input-7-4",label:"JSON body",value:t.object_to_value(t.param_item)},model:{value:t.data,callback:function(e){t.data=e},expression:"data"}})],1)],1)],1),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"error",text:""},on:{click:function(e){return t.delete_static_cache()}}},[t._v(" DELETE ")]),e(l.Z,{attrs:{color:"success",text:""},on:{click:function(e){return t.update_value()}}},[t._v(" UPDATE ")])],1)],1)],1)],1)},ve=[],Ee={props:{param_item:{type:Object,default(){return{}}}},data(){return{dialog:!1,tab:null,params:["JSON"],data:{}}},created(){this.data=this.object_to_value(this.param_item)},mounted(){this.data=this.object_to_value(this.param_item)},methods:{...(0,q.nv)(["REFRESH_CACHES","REFRESH_CURRENT_PROJECT"]),...(0,q.Se)(["GET_CURRENT_PROJECT"]),update_value(){let t=JSON.parse(this.data),e=this.param_item;console.log(t),e.parameters=t,P.patch_cache_static(e).then((t=>{console.log(t.data),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT()),this.dialog=!1}))},delete_static_cache(){let t=this.param_item;P.delete_cache_static(t).then((t=>{console.log(t.data),this.dialog=!1})),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())},object_to_value(t){return console.log(t),JSON.stringify(t.parameters)}}},ge=Ee,Ce=(0,I.Z)(ge,me,ve,!1,null,null,null),fe=Ce.exports,Re=function(){var t=this,e=t._self._c;return e("div",{staticClass:"text-right"},[e(O.Z,{attrs:{width:"90rem",height:"10rem"},scopedSlots:t._u([{key:"activator",fn:function({on:a,attrs:o}){return[e(l.Z,t._g(t._b({attrs:{fab:"",dark:"",small:"",color:"indigo"}},"v-btn",o,!1),a),[e(h.Z,{attrs:{dark:""}},[t._v(" mdi-plus ")])],1)]}}]),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[e(c.Z,[e(d.EB,{staticClass:"text-h5"},[t._v(" Create static cache dialog ")]),e(d.ZB,[e(T.Z,{attrs:{label:"Name"},model:{value:t.param_item.value.name,callback:function(e){t.$set(t.param_item.value,"name",e)},expression:"param_item.value.name"}}),e(c.Z,[e(d.ZB,[e(N.Z,{attrs:{outlined:"",name:"input-7-4",label:"JSON body",value:t.object_to_value(this.param_item.value)},model:{value:t.data,callback:function(e){t.data=e},expression:"data"}})],1)],1)],1),e(u.Z),e(d.h7,[e(R.Z),e(l.Z,{attrs:{color:"success",text:""},on:{click:function(e){return t.create_static()}}},[t._v(" CREATE ")])],1)],1)],1)],1)},Te=[],Ze={props:{},data(){return{dialog:!1,data:{},param_item:{value:{name:"default_1",projectId:"",parameters:{}},key:{project:""}}}},created(){this.data=this.object_to_value(this.param_item.value.parameters)},mounted(){this.data=this.object_to_value(this.param_item.value.parameters)},methods:{...(0,q.nv)(["REFRESH_CACHES","REFRESH_CURRENT_PROJECT"]),...(0,q.Se)(["GET_CURRENT_PROJECT"]),create_static(){let t=JSON.parse(this.data),e=this.param_item;console.log(t),e.value.parameters=t,e.key.project=this.GET_CURRENT_PROJECT().id,e.value.projectId=this.GET_CURRENT_PROJECT().id,P.create_cache_static(e).then((t=>{console.log(t.data),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT()),this.dialog=!1}))},delete_static_cache(){let t=this.param_item;P.delete_cache_static(t).then((t=>{console.log(t.data),this.dialog=!1})),this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())},object_to_value(t){return console.log(t),JSON.stringify(t.parameters)}}},Se=Ze,be=(0,I.Z)(Se,Re,Te,!1,null,null,null),ye=be.exports,ke={components:{StaticInfoDialog:fe,CreateStaticCacheDialog:ye},data:()=>({search:"",headers:[{text:"uuid",align:"start",sortable:!1,value:"cacheUUID"},{text:"projectId",value:"projectId"},{text:"name",value:"name"},{text:"last update date",value:"lastUpdate"},{text:"",value:"actions"}]}),methods:{...(0,q.Se)(["GET_STATIC_CACHES","GET_CURRENT_PROJECT"]),...(0,q.nv)(["REFRESH_CURRENT_PROJECT"])}},xe=ke,je=(0,I.Z)(xe,pe,he,!1,null,null,null),Pe=je.exports,Oe=a(8345);o.ZP.use(Oe.Z);const Ne=[{path:"/",name:"DatapoolCaches",component:Gt},{path:"/cluster",name:"NodeCluster",component:zt},{path:"/storage",name:"DatapoolStorage",component:_e},{path:"/params",name:"StaticParameters",component:Pe}],we=new Oe.Z({mode:"history",routes:Ne});var Je=we;o.ZP.use(q.ZP);var Ue=new q.ZP.Store({state:{current_project:"",projects:[],caches:[],storage:[],static_caches:[]},mutations:{SET_STATIC_CACHE(t,e){t.static_caches=e},SET_CACHES(t,e){t.caches=e},SET_CURRENT_PROJECT(t,e){t.current_project=e},SET_PROJECTS(t,e){t.projects=e},SET_STORAGE(t,e){t.storage=e}},actions:{REFRESH_PROJECTS({commit:t}){P.get_projects().then((e=>{console.log(e.data),t("SET_PROJECTS",e.data),t("SET_CURRENT_PROJECT",e.data[0])}))},REFRESH_CACHES({commit:t},e){P.get_caches(e).then((e=>{console.log(e.data),t("SET_CACHES",e.data)}))},REFRESH_CURRENT_PROJECT({commit:t},e){P.get_caches(e.id).then((a=>{t("SET_CURRENT_PROJECT",e),t("SET_CACHES",a.data)})),P.get_storage(e.id).then((e=>{t("SET_STORAGE",e.data)})),P.get_static_data_caches(e.id).then((e=>{t("SET_STATIC_CACHE",e.data)}))}},getters:{GET_CURRENT_PROJECT(t){return t.current_project},GET_PROJECTS(t){return t.projects},GET_CACHES(t){return t.caches},GET_STORAGE(t){return t.storage},GET_STATIC_CACHES(t){return t.static_caches}}});o.ZP.config.productionTip=!1,o.ZP.use(Je),new o.ZP({vuetify:rt,store:Ue,router:Je,render:t=>t(at)}).$mount("#app")}},e={};function a(o){var r=e[o];if(void 0!==r)return r.exports;var s=e[o]={exports:{}};return t[o](s,s.exports,a),s.exports}a.m=t,function(){var t=[];a.O=function(e,o,r,s){if(!o){var i=1/0;for(d=0;d<t.length;d++){o=t[d][0],r=t[d][1],s=t[d][2];for(var n=!0,l=0;l<o.length;l++)(!1&s||i>=s)&&Object.keys(a.O).every((function(t){return a.O[t](o[l])}))?o.splice(l--,1):(n=!1,s<i&&(i=s));if(n){t.splice(d--,1);var c=r();void 0!==c&&(e=c)}}return e}s=s||0;for(var d=t.length;d>0&&t[d-1][2]>s;d--)t[d]=t[d-1];t[d]=[o,r,s]}}(),function(){a.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return a.d(e,{a:e}),e}}(),function(){a.d=function(t,e){for(var o in e)a.o(e,o)&&!a.o(t,o)&&Object.defineProperty(t,o,{enumerable:!0,get:e[o]})}}(),function(){a.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(t){if("object"===typeof window)return window}}()}(),function(){a.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)}}(),function(){a.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})}}(),function(){var t={143:0};a.O.j=function(e){return 0===t[e]};var e=function(e,o){var r,s,i=o[0],n=o[1],l=o[2],c=0;if(i.some((function(e){return 0!==t[e]}))){for(r in n)a.o(n,r)&&(a.m[r]=n[r]);if(l)var d=l(a)}for(e&&e(o);c<i.length;c++)s=i[c],a.o(t,s)&&t[s]&&t[s][0](),t[s]=0;return a.O(d)},o=self["webpackChunkdatapool_frontend"]=self["webpackChunkdatapool_frontend"]||[];o.forEach(e.bind(null,0)),o.push=e.bind(null,o.push.bind(o))}();var o=a.O(void 0,[998],(function(){return a(7718)}));o=a.O(o)})();
//# sourceMappingURL=app.80d8e2be.js.map