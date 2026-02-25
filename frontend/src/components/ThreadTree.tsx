import React from "react";
import type { ThreadNode } from "./threadTypes";

type Props = {
  roots: ThreadNode[];
  onClickAudio: (node: ThreadNode) => void;

  selectedId?: string;
  onSelectNode?: (node: ThreadNode) => void;
};

export default function ThreadTree({ roots, onClickAudio, selectedId, onSelectNode }: Props) {
  return (
    <div className="bl-threadlist">
      {roots.map((n) => (
        <ThreadNodeView
          key={n.id}
          node={n}
          depth={0}
          onClickAudio={onClickAudio}
          selectedId={selectedId}
          onSelectNode={onSelectNode}
        />
      ))}
    </div>
  );
}

function ThreadNodeView({
  node,
  depth,
  onClickAudio,
  selectedId,
  onSelectNode,
}: {
  node: ThreadNode;
  depth: number;
  onClickAudio: (node: ThreadNode) => void;
  selectedId?: string;
  onSelectNode?: (node: ThreadNode) => void;
}) {
  const isSelected = selectedId === String(node.id);

  return (
    <div className="bl-threadnode">
      <div
        className={[
          "bl-threadcard",
          isSelected ? "bl-threadcard-selected" : "",
          node.itemType === "AUDIO" ? "bl-threadcard-audio" : "bl-threadcard-comment",
        ].join(" ")}
        style={{ marginLeft: Math.min(depth * 12, 60) }}
        onClick={() => onSelectNode?.(node)}
        role="button"
        tabIndex={0}
        onKeyDown={(e) => {
          if (e.key === "Enter" || e.key === " ") onSelectNode?.(node);
        }}
      >
        <div className="bl-threadcard-top">
          <div className="bl-threadcard-meta">
            <span className="bl-badge">{node.itemType}</span>
            <span className="bl-threadcard-muted">{node.parentItemId ? "Reply" : "Root"}</span>
          </div>

          <div className="bl-threadcard-actions" onClick={(e) => e.stopPropagation()}>
            {node.itemType === "AUDIO" ? (
              <button
                className="bl-button bl-button-ghost"
                onClick={() => {
                  // keep Studio selection in sync with "Lineage" clicks
                  onSelectNode?.(node);
                  onClickAudio(node);
                }}
              >
                Lineage
              </button>
            ) : (
              <button className="bl-button bl-button-ghost" disabled title="Coming soon">
                Reply
              </button>
            )}
          </div>
        </div>

        {node.itemType === "COMMENT" && <div className="bl-threadcard-body">{node.body}</div>}

        {node.itemType === "AUDIO" && <div className="bl-waveform-placeholder" />}

        <div className="bl-threadcard-footer">
          <span className="bl-mono bl-threadcard-id">id: {String(node.id).slice(0, 8)}…</span>
        </div>
      </div>

      {node.children.length > 0 && (
        <div className="bl-threadchildren">
          {node.children.map((c) => (
            <ThreadNodeView
              key={c.id}
              node={c}
              depth={depth + 1}
              onClickAudio={onClickAudio}
              selectedId={selectedId}
              onSelectNode={onSelectNode}
            />
          ))}
        </div>
      )}
    </div>
  );
}