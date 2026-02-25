import type { ThreadItemResponse } from "../api";

export type ThreadNode = ThreadItemResponse & {
  children: ThreadNode[];
};
