import type { MockMethod } from "vite-plugin-mock";

export default [
  {
    url: "/files/upload",
    method: "post",
    response: ({ body }) => {
      console.log("body", body);
      return {
        id: "123456",
        status: "312"
      };
    }
  }
] as MockMethod[];
