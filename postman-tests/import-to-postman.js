const fs = require('fs');
const path = require('path');

// Path to the OpenAPI specification file
const openApiPath = path.join(__dirname, '..', 'v3_api-docs.json');
// Path to save the Postman collection
const outputPath = path.join(__dirname, 'domicare-tests.postman_collection.json');

// Function to convert OpenAPI spec to Postman collection
function convertOpenApiToPostman(openApiSpec) {
  const postmanCollection = {
    info: {
      _postman_id: generateUUID(),
      name: openApiSpec.info.title || 'DomiCare API Tests',
      description: openApiSpec.info.description || 'API tests for DomiCare application',
      schema: "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
    },
    item: [],
    event: [],
    variable: []
  };

  // Group endpoints by tags
  const endpointsByTag = {};

  // Process each path
  Object.keys(openApiSpec.paths).forEach(path => {
    const pathObj = openApiSpec.paths[path];

    // Process each method (GET, POST, PUT, DELETE, etc.)
    Object.keys(pathObj).forEach(method => {
      const endpointObj = pathObj[method];
      const tags = endpointObj.tags || ['no-tag'];

      tags.forEach(tag => {
        if (!endpointsByTag[tag]) {
          endpointsByTag[tag] = [];
        }

        // Create a Postman request
        const request = {
          name: endpointObj.summary || endpointObj.operationId || `${method.toUpperCase()} ${path}`,
          request: {
            method: method.toUpperCase(),
            header: [],
            url: {
              raw: `{{baseUrl}}${path}`,
              host: ["{{baseUrl}}"],
              path: path.split('/').filter(p => p)
            }
          },
          response: []
        };

        // Add description if available
        if (endpointObj.description) {
          request.request.description = endpointObj.description;
        }

        // Add query parameters
        if (endpointObj.parameters) {
          const queryParams = endpointObj.parameters.filter(param => param.in === 'query');
          if (queryParams.length > 0) {
            request.request.url.query = queryParams.map(param => ({
              key: param.name,
              value: param.example || '',
              description: param.description || '',
              disabled: !param.required
            }));
          }

          // Add path parameters
          const pathParams = endpointObj.parameters.filter(param => param.in === 'path');
          if (pathParams.length > 0) {
            pathParams.forEach(param => {
              // Replace path parameter placeholders in the URL
              const placeholder = `{${param.name}}`;
              if (request.request.url.raw.includes(placeholder)) {
                request.request.url.raw = request.request.url.raw.replace(
                  placeholder, 
                  `{{${param.name}}}`
                );
              }
              
              // Update the path segments
              request.request.url.path = request.request.url.path.map(segment => 
                segment.includes(`{${param.name}}`) ? `{{${param.name}}}` : segment
              );
            });
          }
        }

        // Add request body
        if (endpointObj.requestBody && endpointObj.requestBody.content) {
          const contentType = Object.keys(endpointObj.requestBody.content)[0];
          request.request.header.push({
            key: 'Content-Type',
            value: contentType
          });

          const bodySchema = endpointObj.requestBody.content[contentType].schema;
          if (bodySchema) {
            request.request.body = {
              mode: 'raw',
              raw: JSON.stringify(generateExampleFromSchema(bodySchema, openApiSpec.components.schemas), null, 2),
              options: {
                raw: {
                  language: contentType.includes('json') ? 'json' : 'text'
                }
              }
            };
          }
        }

        // Add tests for response validation
        if (endpointObj.responses) {
          const successResponse = endpointObj.responses['200'];
          if (successResponse) {
            const testName = `${request.name} successful`;
            const test = `pm.test("${testName}", function () {
              pm.response.to.have.status(200);
              
              const responseData = pm.response.json();
              pm.expect(responseData).to.be.an('object');
              pm.expect(responseData).to.have.property('code');
              
              // Store response data if needed
              if (responseData.data) {
                pm.environment.set('latest_response_data', JSON.stringify(responseData.data));
              }
            });`;
            
            if (!request.event) request.event = [];
            request.event.push({
              listen: 'test',
              script: {
                type: 'text/javascript',
                exec: test.split('\n')
              }
            });
          }
        }

        // Add authorization header if needed
        request.request.auth = {
          type: 'bearer',
          bearer: [
            {
              key: 'token',
              value: '{{auth_token}}',
              type: 'string'
            }
          ]
        };

        endpointsByTag[tag].push(request);
      });
    });
  });

  // Create folders for each tag
  Object.keys(endpointsByTag).forEach(tag => {
    postmanCollection.item.push({
      name: formatTagName(tag),
      item: endpointsByTag[tag]
    });
  });

  return postmanCollection;
}

// Helper function to format tag names
function formatTagName(tag) {
  return tag.split('-')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
}

// Helper function to generate example data from schema
function generateExampleFromSchema(schema, schemas) {
  if (!schema) return {};
  
  // Handle $ref
  if (schema.$ref) {
    const refName = schema.$ref.split('/').pop();
    if (schemas && schemas[refName]) {
      return generateExampleFromSchema(schemas[refName], schemas);
    }
    return {};
  }
  
  // Handle arrays
  if (schema.type === 'array' && schema.items) {
    return [generateExampleFromSchema(schema.items, schemas)];
  }
  
  // Handle objects
  if (schema.type === 'object' || schema.properties) {
    const example = {};
    if (schema.properties) {
      Object.keys(schema.properties).forEach(propName => {
        example[propName] = generateExampleFromSchema(schema.properties[propName], schemas);
      });
    }
    return example;
  }
  
  // Handle primitive types
  switch (schema.type) {
    case 'string':
      return schema.example || schema.default || 'string';
    case 'number':
    case 'integer':
      return schema.example || schema.default || 0;
    case 'boolean':
      return schema.example || schema.default || false;
    default:
      return schema.example || schema.default || {};
  }
}

// Helper function to generate a UUID
function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0;
    const v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

// Main execution
try {
  console.log('Reading OpenAPI specification...');
  const openApiData = fs.readFileSync(openApiPath, 'utf8');
  const openApiSpec = JSON.parse(openApiData);
  
  console.log('Converting to Postman collection...');
  const postmanCollection = convertOpenApiToPostman(openApiSpec);
  
  console.log('Writing Postman collection to file...');
  fs.writeFileSync(outputPath, JSON.stringify(postmanCollection, null, 2));
  
  console.log(`Postman collection generated successfully: ${outputPath}`);
  
  // Create environment file with variables
  const environmentFile = path.join(__dirname, 'domicare-environment.json');
  const environment = {
    id: generateUUID(),
    name: 'DomiCare Environment',
    values: [
      {
        key: 'baseUrl',
        value: openApiSpec.servers[0]?.url || 'http://localhost:8080',
        enabled: true
      },
      {
        key: 'auth_token',
        value: '',
        enabled: true
      }
    ],
    _postman_variable_scope: 'environment'
  };
  
  fs.writeFileSync(environmentFile, JSON.stringify(environment, null, 2));
  console.log(`Environment file generated: ${environmentFile}`);
  
} catch (error) {
  console.error('Error:', error.message);
}
