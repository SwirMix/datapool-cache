<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="90rem"
      height="10rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-icon
          medium
          v-bind="attrs"
          v-on="on"
        >
          mdi-information
        </v-icon>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          {{param_item.name}} [{{param_item.cacheUUID}}]
        </v-card-title>
        <v-card-text>
          <h3><b>lastUpdate:</b> {{param_item.lastUpdate}}</h3>
          <h3><b>name:</b> {{param_item.name}}</h3>
          <h3><b>cacheUUID:</b> {{param_item.cacheUUID}}</h3>
          <h3><b>projectId:</b> {{param_item.projectId}}</h3>
              <v-card>
                <v-card-text>
                    <v-textarea
                      outlined
                      name="input-7-4"
                      label="JSON body"
                      :value="object_to_value(param_item)"
                      v-model="data"
                    ></v-textarea>
                </v-card-text>
              </v-card>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="error"
            text
            @click="delete_static_cache()"
          >
            DELETE
          </v-btn>
          <v-btn
            color="success"
            text
            @click="update_value()"
          >
            UPDATE
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
  import {mapActions} from 'vuex'
  import {mapGetters} from 'vuex'
  import apiClient from '@/plugins/backendClient.js'

  export default {
    props: {
      param_item: {
        type:Object,
        default(){
          return {}
        }
      }
    },
    data () {
      return {
        dialog: false,
        tab: null,
        params: [
          'JSON'
        ],
        data: {},
      }
    },
    created(){
        this.data = this.object_to_value(this.param_item)
    },
    mounted(){
        this.data = this.object_to_value(this.param_item)
    },
    methods: {
      ...mapActions([
         'REFRESH_CACHES',
         'REFRESH_CURRENT_PROJECT'
      ]),
      ...mapGetters([
        'GET_CURRENT_PROJECT'
      ]),
      update_value(){
        let data = JSON.parse(this.data)
        let request = this.param_item
        console.log(data)
        request.parameters = data
        apiClient.patch_cache_static(request).then((response)=>{
           console.log(response.data)
           this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())
           this.dialog = false
        })
      },

      delete_static_cache(){
        let request = this.param_item
        apiClient.delete_cache_static(request).then((response)=>{
           console.log(response.data)
           this.dialog = false
        })
        this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())
      },

      object_to_value(item){
        console.log(item)
        return JSON.stringify(item.parameters);
      }
    }
  }
</script>