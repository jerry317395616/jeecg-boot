<template>
    <view>
        <!--标题和返回-->
		<cu-custom :bgColor="NavBarColor" isBack :backRouterName="backRouteName">
			<block slot="backText">返回</block>
			<block slot="content">签到机</block>
		</cu-custom>
		 <!--表单区域-->
		<view>
			<form>
              <view class="cu-form-group">
                <view class="flex align-center">
                  <view class="title"><text space="ensp">科室：</text></view>
                  <input  placeholder="请输入科室" v-model="model.department"/>
                </view>
              </view>
              <view class="cu-form-group">
                <view class="flex align-center">
                  <view class="title"><text space="ensp">ip地址：</text></view>
                  <input  placeholder="请输入ip地址" v-model="model.ipAddress"/>
                </view>
              </view>
              <view class="cu-form-group">
                <view class="flex align-center">
                  <view class="title"><text space="ensp">mac地址：</text></view>
                  <input  placeholder="请输入mac地址" v-model="model.macAddress"/>
                </view>
              </view>
              <view class="cu-form-group">
                <view class="flex align-center">
                  <view class="title"><text space="ensp">诊区：</text></view>
                  <input  placeholder="请输入诊区" v-model="model.clinicArea"/>
                </view>
              </view>
              <view class="cu-form-group">
                <view class="flex align-center">
                  <view class="title"><text space="ensp">可签到科室：</text></view>
                  <input  placeholder="请输入可签到科室" v-model="model.signableDept"/>
                </view>
              </view>
				<view class="padding">
					<button class="cu-btn block bg-blue margin-tb-sm lg" @click="onSubmit">
						<text v-if="loading" class="cuIcon-loading2 cuIconfont-spin"></text>提交
					</button>
				</view>
			</form>
		</view>
    </view>
</template>

<script>
    import myDate from '@/components/my-componets/my-date.vue'

    export default {
        name: "CheckMachineForm",
        components:{ myDate },
        props:{
          formData:{
              type:Object,
              default:()=>{},
              required:false
          }
        },
        data(){
            return {
				CustomBar: this.CustomBar,
				NavBarColor: this.NavBarColor,
				loading:false,
                model: {},
                backRouteName:'index',
                url: {
                  queryById: "/check/checkMachine/queryById",
                  add: "/check/checkMachine/add",
                  edit: "/check/checkMachine/edit",
                },
            }
        },
        created(){
             this.initFormData();
        },
        methods:{
           initFormData(){
               if(this.formData){
                    let dataId = this.formData.dataId;
                    this.$http.get(this.url.queryById,{params:{id:dataId}}).then((res)=>{
                        if(res.data.success){
                            console.log("表单数据",res);
                            this.model = res.data.result;
                        }
                    })
                }
            },
            onSubmit() {
                let myForm = {...this.model};
                this.loading = true;
                let url = myForm.id?this.url.edit:this.url.add;
				this.$http.post(url,myForm).then(res=>{
				   console.log("res",res)
				   this.loading = false
				   this.$Router.push({name:this.backRouteName})
				}).catch(()=>{
					this.loading = false
				});
            }
        }
    }
</script>
