<template>
  <div class="duty-item">
    <div class="item-row">
      <div class="row-left portal-font-color-lv1">
        {{ $Lan(lanFunName, "responsibleDept", "责任部门") }}：
      </div>
      <div class="row-right">
        {{ $Lan(lanFunName, "deansOffice", "教务处") }}&{{
          $Lan(lanFunName, "teacherDevelopCenter", "教师发展中心")
        }}&{{ $Lan(lanFunName, "eduTechnologyCenter", "教育技术中心") }}
      </div>
    </div>
    <div class="item-row">
      <div class="row-left portal-font-color-lv1">
        {{ $Lan(lanFunName, "responsibilities", "职责内容") }}：
      </div>
      <div class="row-right">
        {{ $Lan(lanFunName, "deansOffice", "教务处") }}&{{
          $Lan(lanFunName, "teacherDevelopCenter", "教师发展中心")
        }}&{{ $Lan(lanFunName, "eduTechnologyCenter", "教育技术中心") }}
      </div>
    </div>
    <div class="item-row">
      <div class="row-left portal-font-color-lv1">
        {{ $Lan(lanFunName, "postName", "岗位名称") }}：
      </div>
      <div class="row-right">
        {{ $Lan(lanFunName, "deansOffice", "教务处") }}&{{
          $Lan(lanFunName, "teacherDevelopCenter", "教师发展中心")
        }}&{{ $Lan(lanFunName, "eduTechnologyCenter", "教育技术中心") }}
      </div>
    </div>
    <div class="item-row">
      <div class="row-left portal-font-color-lv1">
        {{ $Lan(lanFunName, "relatedItems", "关联事项") }}：
      </div>
      <div class="row-right event-item  portal-primary-color-lv1">
        {{ $Lan(lanFunName, "deansOffice", "教务处") }}&{{
          $Lan(lanFunName, "teacherDevelopCenter", "教师发展中心")
        }}&{{ $Lan(lanFunName, "eduTechnologyCenter", "教育技术中心") }}
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less" scoped>
.duty-item {
  padding: 10px 0;
  border-bottom: 1px dashed #e7edf1;
  .item-row {
    margin-bottom: 6px;
    display: flex;
    height: 24px;
    line-height: 24px;
    font-size: 14px;
    .row-left {
      width: 70px;

      color: #102645;
      font-weight: bold;
    }
    .row-right {
      flex: 1;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      padding: 0 16px;
      &.event-item {
        cursor: pointer;
      }
    }
  }
}
</style>
