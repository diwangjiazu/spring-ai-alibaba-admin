import React from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

const MarkdownRenderer = ({ content }) => {
  return (
    <ReactMarkdown
      remarkPlugins={[remarkGfm]}
      components={{
        // 自定义代码块样式
        code({ node, inline, className, children, ...props }) {
          return inline ? (
            <code
              style={{
                backgroundColor: '#f5f5f5',
                padding: '2px 6px',
                borderRadius: '3px',
                fontFamily: 'monospace',
                fontSize: '0.9em',
                color: '#d73a49'
              }}
              {...props}
            >
              {children}
            </code>
          ) : (
            <pre
              style={{
                backgroundColor: '#f6f8fa',
                padding: '12px',
                borderRadius: '6px',
                overflow: 'auto',
                border: '1px solid #e1e4e8',
                marginTop: '8px',
                marginBottom: '8px'
              }}
            >
              <code
                style={{
                  fontFamily: 'monospace',
                  fontSize: '0.9em',
                  color: '#24292e'
                }}
                {...props}
              >
                {children}
              </code>
            </pre>
          );
        },
        // 自定义表格样式
        table({ children, ...props }) {
          return (
            <div style={{ overflowX: 'auto', marginTop: '8px', marginBottom: '8px' }}>
              <table
                style={{
                  borderCollapse: 'collapse',
                  width: '100%',
                  border: '1px solid #e1e4e8'
                }}
                {...props}
              >
                {children}
              </table>
            </div>
          );
        },
        th({ children, ...props }) {
          return (
            <th
              style={{
                padding: '8px 12px',
                backgroundColor: '#f6f8fa',
                border: '1px solid #e1e4e8',
                fontWeight: 'bold',
                textAlign: 'left'
              }}
              {...props}
            >
              {children}
            </th>
          );
        },
        td({ children, ...props }) {
          return (
            <td
              style={{
                padding: '8px 12px',
                border: '1px solid #e1e4e8'
              }}
              {...props}
            >
              {children}
            </td>
          );
        },
        // 自定义链接样式
        a({ children, ...props }) {
          return (
            <a
              style={{
                color: '#1890ff',
                textDecoration: 'none'
              }}
              target="_blank"
              rel="noopener noreferrer"
              {...props}
            >
              {children}
            </a>
          );
        },
        // 自定义标题样式
        h1({ children, ...props }) {
          return (
            <h1
              style={{
                fontSize: '1.5em',
                fontWeight: 'bold',
                marginTop: '16px',
                marginBottom: '8px',
                borderBottom: '1px solid #e1e4e8',
                paddingBottom: '8px'
              }}
              {...props}
            >
              {children}
            </h1>
          );
        },
        h2({ children, ...props }) {
          return (
            <h2
              style={{
                fontSize: '1.3em',
                fontWeight: 'bold',
                marginTop: '14px',
                marginBottom: '8px'
              }}
              {...props}
            >
              {children}
            </h2>
          );
        },
        h3({ children, ...props }) {
          return (
            <h3
              style={{
                fontSize: '1.1em',
                fontWeight: 'bold',
                marginTop: '12px',
                marginBottom: '6px'
              }}
              {...props}
            >
              {children}
            </h3>
          );
        },
        // 自定义列表样式
        ul({ children, ...props }) {
          return (
            <ul
              style={{
                paddingLeft: '20px',
                marginTop: '8px',
                marginBottom: '8px'
              }}
              {...props}
            >
              {children}
            </ul>
          );
        },
        ol({ children, ...props }) {
          return (
            <ol
              style={{
                paddingLeft: '20px',
                marginTop: '8px',
                marginBottom: '8px'
              }}
              {...props}
            >
              {children}
            </ol>
          );
        },
        // 自定义引用样式
        blockquote({ children, ...props }) {
          return (
            <blockquote
              style={{
                borderLeft: '4px solid #dfe2e5',
                paddingLeft: '16px',
                marginLeft: '0',
                marginTop: '8px',
                marginBottom: '8px',
                color: '#6a737d'
              }}
              {...props}
            >
              {children}
            </blockquote>
          );
        },
        // 自定义段落样式
        p({ children, ...props }) {
          return (
            <p
              style={{
                marginTop: '8px',
                marginBottom: '8px',
                lineHeight: '1.6'
              }}
              {...props}
            >
              {children}
            </p>
          );
        }
      }}
    >
      {content}
    </ReactMarkdown>
  );
};

export default MarkdownRenderer;
