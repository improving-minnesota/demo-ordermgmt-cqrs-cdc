import { NextResponse } from 'next/server';
import { apiConfig, mockData, isDevelopment } from '../../config';

// Construct the full remote API URL from the configuration
const REMOTE_API_URL = `${apiConfig.remoteApiBaseUrl}${apiConfig.endpoints.payment}`;

export async function POST(request: Request) {
  try {
    // Parse the incoming request
    const body = await request.json();
    const { orderId } = body;
    
    // Validate orderId
    if (!orderId) {
      console.warn('Payment API: No orderId provided');
    }
    
    // Prepare the data to send to the remote API
    const requestData = {
      orderId: orderId || '',
      paymentType: "Credit Card",
      creditCardType: "Magical Visa",
      creditCardNumber: "7777-7777-7777-7777",
      amount: 0.01
    };
    
    // Log the request details
    console.log(`Payment API request (${isDevelopment ? 'DEV' : 'PROD'} mode)`);
    console.log('Request data:', JSON.stringify(requestData, null, 2));
    
    // Use mock data in development mode if configured
    if (apiConfig.useMockData) {
      console.log('Using mock data for payment request (development mode)');
      
      // Simulate network delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Update mock data with actual order ID if available
      const responseData = {
        ...mockData.payment,
        data: {
          ...mockData.payment.data,
          orderId: orderId || mockData.payment.data.orderId,
          transactionId: `tx-${Date.now().toString(36)}`
        }
      };
      
      // Return mock response
      return NextResponse.json(responseData);
    }
    
    // In production mode or when mock is disabled, make the actual API call
    console.log('Proxying payment request to:', REMOTE_API_URL);
    
    // Forward the request to the remote API with timeout
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), apiConfig.timeoutMs);
    
    try {
      const response = await fetch(REMOTE_API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
        signal: controller.signal,
      });
      
      clearTimeout(timeoutId);
      
      // Parse the remote API response
      const responseData = await response.json();
      
      // Check if the response was successful
      if (!response.ok) {
        throw new Error(responseData.message || 'Remote API request failed');
      }
      
      // Return the remote API response to the client
      return NextResponse.json({
        success: true,
        message: 'Payment processed successfully',
        data: responseData,
      });
    } catch (error) {
      // Handle fetch-specific errors
      const fetchError = error as { name?: string };
      if (fetchError.name === 'AbortError') {
        throw new Error(`Request to ${REMOTE_API_URL} timed out after ${apiConfig.timeoutMs}ms`);
      }
      throw error;
    }
  } catch (error) {
    console.error('Payment API error:', error);
    return NextResponse.json(
      { success: false, message: 'Failed to process payment' },
      { status: 500 }
    );
  }
}
