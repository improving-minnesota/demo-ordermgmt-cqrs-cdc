/**
 * Application configuration based on environment
 */

/**
 * Determines if we're running in a development environment
 */
export const isDevelopment = process.env.NODE_ENV === 'development';

/**
 * API configuration
 */
export const apiConfig = {
  // Base URL for the remote API
  remoteApiBaseUrl: process.env.REMOTE_API_BASE_URL || 'http://snack-order-commands:8800',
  
  // Flag to use mock data instead of making actual API calls (useful for local development)
  useMockData: isDevelopment && (process.env.USE_MOCK_API === 'true' || !process.env.USE_MOCK_API),
  
  // Timeout duration for API requests in milliseconds
  timeoutMs: parseInt(process.env.API_TIMEOUT_MS || '5000', 10),
  
  // Endpoints
  endpoints: {
    itemDetails: '/api/item-details',
    shippingLocation: '/api/shipping-location',
    payment: '/api/payment',
  }
};

/**
 * Mock data for development
 */
export const mockData = {
  promotion: {
    success: true,
    message: 'Promotion request processed successfully (MOCK)',
    data: {
      orderId: 'mock-order-123',
      status: 'success',
      items: [
        {
          itemId: '1',
          itemName: 'Snickers',
          price: 0.01,
          quantity: 1,
          processedAt: new Date().toISOString()
        }
      ]
    }
  },
  location: {
    success: true,
    message: 'Shipping location processed successfully (MOCK)',
    data: {
      orderId: 'mock-order-123',
      customerName: 'Mock User',
      customerAddress: '2115 Summit Ave.',
      zipCode: '55105',
      latitude: 44.9374829167,
      longitude: -93.1893875758,
      locationProcessedAt: new Date().toISOString()
    }
  },
  payment: {
    success: true,
    message: 'Payment processed successfully (MOCK)',
    data: {
      orderId: 'mock-order-123',
      paymentType: 'Credit Card',
      creditCardType: 'Magical Visa',
      creditCardNumber: '7777-7777-7777-7777',
      amount: 0.01,
      transactionId: `tx-${Date.now().toString(36)}`,
      paymentStatus: 'approved',
      paymentProcessedAt: new Date().toISOString()
    }
  }
};
