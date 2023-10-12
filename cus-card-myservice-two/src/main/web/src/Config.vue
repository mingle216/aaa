<template>
  <we-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" labwe-width="100px" class="demo-ruleForm">
    <we-form-item label="cardLink密码" prop="pass">
      <we-input type="password" v-model="ruleForm.pass" autocomplete="off"></we-input>
    </we-form-item>
    <we-form-item label="cardLink确认密码" prop="checkPass">
      <we-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></we-input>
    </we-form-item>
    <we-form-item label="cardLink年龄" prop="age">
      <we-input v-model.number="ruleForm.age"></we-input>
    </we-form-item>
    <we-form-item>
      <we-button type="primary" @click="submitForm('ruleForm')">提交</we-button>
      <we-button @click="resetForm('ruleForm')">重置</we-button>
    </we-form-item>
  </we-form>
</template>

<script>
export default {
  data() {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'));
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入数字值'));
        } else {
          if (value < 18) {
            callback(new Error('必须年满18岁'));
          } else {
            callback();
          }
        }
      }, 1000);
    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        pass: '',
        checkPass: '',
        age: ''
      },
      rules: {
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        age: [
          { validator: checkAge, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log('form submit: emit event')
          this.$emit('submit', this.ruleForm)
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>
