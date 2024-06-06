import { createAPI } from "@/utils/request";

// 查询部门列表
export const list = (data) => createAPI("/company/departments", "get", data);
// 保存部门
// data  {id：“”，name：“”}
export const save = (data) => createAPI("/company/departments", "post", data);
// 根据id查询部门 {id：“”}
export const find = (data) =>
  createAPI(`/company/departments/${data.id}`, "get", data);
// 根据id删除部门 {id:""}
export const deleteById = (data) =>
  createAPI(`/company/departments/${data.id}`, "delete", data);
// 根据id更新部门 {id:"",name:"",code:""}
export const update = (data) =>
  createAPI(`/company/departments/${data.id}`, "put", data);
// 保存或更新的方法
export const saveOrupdate = (data) => {
  return data.id ? update(data) : save(data);
};
