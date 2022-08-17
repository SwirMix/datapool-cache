import axios from 'axios'

var apiClient = {
  server_endpoint: 'http://localhost:8086/',
  api: {
     post_auth: 'api/v1/account/auth',
     remote_token_auth: 'api/v1/account/remote/auth',
     get_projects: 'api/v1/projects/',
     get_caches: 'api/v1/caches',
     get_storage: 'api/v1/storage',
     post_reload_cache: 'api/v1/storage/reload',
     post_reload_internal_cache: 'api/v1/datapool/cache',
     delete_cache: 'api/v1/datapool/cache',
     delete_file: 'api/v1/storage',
     post_upload_file: 'api/v1/storage/upload',
     post_import_cache_to_file: 'api/v1/storage/import',
     post_cache_from_jdbc: 'api/v1/datapool/cache/create',
     get_cluster: 'api/v1/cluster',
     post_create_project: 'api/v1/projects/create',
     delete_project: 'api/v1/projects/delete',
     get_static_caches: 'api/v1/static/caches',
     patch_static_cache: 'api/v1/datapool/static/update',
     delete_static_cache: 'api/v1/datapool/static/single',
     create_static_cache: 'api/v1/static/create'
  },

  create_cache_static(body){
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    return axios.post(this.server_endpoint + this.api.create_static_cache , body, config)
  },

  delete_cache_static(body){
    var config = {
        headers: {
            'content-type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'DELETE'
    };
    return axios(this.server_endpoint + this.api.delete_static_cache + '?cacheUuid=' + body.cacheUUID + '&projectId=' + body.projectId, config);
  },

  patch_cache_static(body){
    let request = {
                    value: {
                      name: body.name,
                      projectId: body.projectId,
                      parameters: body.parameters
                    },
                    key: {
                      project: body.projectId,
                      cacheUUID: body.cacheUUID
                    }
                  }
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'PATCH',
    };
    return axios.patch(this.server_endpoint + this.api.patch_static_cache , request, config)
  },

  post_create_project(name, description){
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    let body = {
        name: name,
        description: description
    }
    return axios.post(this.server_endpoint + this.api.post_create_project , body, config)
  },

  auth(request) {
    console.log(this.type);
    var config = {
        headers: {'content-type': 'application/json'},
        method: 'POST'
    };
    return axios.post(this.server_endpoint + this.api.post_auth, request, config)
  },

  remote_auth() {
    console.log(this.type);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST'
    };
    return axios.get(this.server_endpoint + this.api.remote_token_auth, config)
  },

  get_cluster_info() {
    console.log(this.type);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'GET'
    };
    return axios.get(this.server_endpoint + this.api.get_cluster, config)
  },

  get_static_data_caches(projectId) {
    console.log(this.type);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'GET'
    };
    return axios.get(this.server_endpoint + this.api.get_static_caches + '?projectId=' + projectId, config)
  },


  get_projects() {
    var config = {
        headers: {
            'content-type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'GET'
    };
    return axios(this.server_endpoint + this.api.get_projects, config);
  },
  get_caches(project) {
    var config = {
        headers: {
            'content-type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'GET'
    };
    return axios(this.server_endpoint + this.api.get_caches + '?project=' + project, config);
  },
  get_storage(project) {
    var config = {
        headers: {
            'content-type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'GET'
    };
    return axios(this.server_endpoint + this.api.get_storage + '?storage_id=' + project, config);
  },
  delete_cache(cacheName){
    var config = {
        headers: {
            'content-type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'DELETE'
    };
    return axios(this.server_endpoint + this.api.delete_cache + '/' + cacheName, config);
  },

  delete_project_request(projectId){
    var config = {
        headers: {
            'content-type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'DELETE'
    };
    return axios(this.server_endpoint + this.api.delete_project + '/' + projectId, config);
  },

  delete_file(project, file){
    var config = {
        headers: {
            'content-type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'DELETE'
    };
    return axios(this.server_endpoint + this.api.delete_file + '/' + project + '/' + file, config);
  },

  import_cache_request(cache, fileName){
   console.log(this.type);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    let data = {
      cacheName: cache.cacheName,
      fileName: fileName,
      override: true,
      project: cache.baseProject
    }
    return axios.post(this.server_endpoint + this.api.post_import_cache_to_file, data, config)
  },

  post_reload_cache(project_id, fileName){
   console.log(this.type);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    let data = {}
    return axios.post(this.server_endpoint + this.api.post_reload_cache + '?fileName=' + fileName + '&project=' + project_id, data, config)
  },

  post_reload_all(project_id){
   console.log(this.type);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    let data = {}
    return axios.post(this.server_endpoint + this.api.post_reload_cache + '?project=' + project_id, data, config)
  },

  post_reload_internal_cache(cacheName){
   console.log(this.type);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    let data = {}
    return axios.post(this.server_endpoint + this.api.post_reload_internal_cache + '/' + cacheName, data, config)
  },
  post_upload_file(file, project_id){
   console.log(this.file);
    var config = {
        headers: {
            'content-type': 'multipart/form-data',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    return axios.post(this.server_endpoint + this.api.post_upload_file + '?project=' + project_id , file, config)
  },

  post_jdbc_cache(body){
   console.log(body);
    var config = {
        headers: {
            'content-type': 'application/json',
            'token': localStorage.getItem('http.datapool.token')
        },
        method: 'POST',
    };
    return axios.post(this.server_endpoint + this.api.post_cache_from_jdbc , body, config)
  }
};

export default apiClient