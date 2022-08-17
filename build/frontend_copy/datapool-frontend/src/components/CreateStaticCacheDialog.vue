<template>
  <div class="text-right">
    <v-dialog
      v-model="dialog"
      width="90rem"
      height="10rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          fab
          dark
          small
          v-bind="attrs"
          v-on="on"
          color="indigo"
        >
          <v-icon dark>
            mdi-plus
          </v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="text-h5">
            Create static cache dialog
        </v-card-title>
        <v-card-text>
              <v-text-field
                label="Name"
                v-model="param_item.value.name"
              ></v-text-field>
              <v-card>
                <v-card-text>
                    <v-textarea
                      outlined
                      name="input-7-4"
                      label="JSON body"
                      :value="object_to_value(this.param_item.value)"
                      v-model="data"
                    ></v-textarea>
                </v-card-text>
              </v-card>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="success"
            text
            @click="create_static()"
          >
            CREATE
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
    },
    data () {
      return {
        dialog: false,
        data: {},
        param_item: {
                      value: {
                        name: 'default_1',
                        projectId: '',
                        parameters: {}
                      },
                      key: {
                        project: ''
                      }
                    }
      }
    },
    created(){
        this.data = this.object_to_value(this.param_item.value.parameters)
    },
    mounted(){
        this.data = this.object_to_value(this.param_item.value.parameters)
    },
    methods: {
      ...mapActions([
         'REFRESH_CACHES',
         'REFRESH_CURRENT_PROJECT'
      ]),
      ...mapGetters([
        'GET_CURRENT_PROJECT'
      ]),
      create_static(){
        let data = JSON.parse(this.data)
        let request = this.param_item
        console.log(data)
        request.value.parameters = data
        request.key.project = this.GET_CURRENT_PROJECT().id
        request.value.projectId = this.GET_CURRENT_PROJECT().id
        apiClient.create_cache_static(request).then((response)=>{
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