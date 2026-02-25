import type { ThreadItemResponse } from "../api";
import type { ThreadNode } from "./threadTypes";

export function buildThreadTree(items: ThreadItemResponse[]): ThreadNode[] {
  const map = new Map<string, ThreadNode>();

  // initialize nodes
  for (const item of items) {
    map.set(item.id, { ...item, children: [] });
  }

  // attach children
  const roots: ThreadNode[] = [];
  for (const node of map.values()) {
    if (node.parentItemId) {
      const parent = map.get(node.parentItemId);
      if (parent) parent.children.push(node);
      else roots.push(node); // orphan fallback
    } else {
      roots.push(node);
    }
  }

  // stable ordering: createdAt asc for each sibling list
  const sortRec = (nodes: ThreadNode[]) => {
    nodes.sort((a, b) => a.createdAt.localeCompare(b.createdAt));
    for (const n of nodes) sortRec(n.children);
  };
  sortRec(roots);

  return roots;
}
