-- Customer Order Aggregate - JOIN
INSERT INTO customer_order_aggregate
SELECT
    o.order_id,
    CASE
        WHEN o.item_status = 0 AND p.payment_type is null THEN 'PENDING'
        WHEN o.item_status = 1 THEN 'FULFILLED'
        WHEN o.item_status = 2 THEN 'SHIPPED'
        ELSE 'PAID'
    END,
    ARRAY_AGG(ROW(
            o.item_id,
            o.item_name,
            CASE
                WHEN o.item_status = 0 AND p.payment_type is null THEN 'PENDING'
                WHEN o.item_status = 1 THEN 'FULFILLED'
                WHEN o.item_status = 2 THEN 'SHIPPED'
                ELSE 'PAID'
            END,
            ROW(o.price, IF(o.currency = 0, 'USD', 'USD')),
            o.quantity)) as items,
    ROW(
        s.customer_name,
        s.customer_address,
        s.zip_code,
        ROW(s.latitude, s.longitude)) as shippingLocation,
    CASE
        WHEN p.payment_type is null THEN
            NULL
        ELSE
            ROW(
                p.payment_type,
                p.credit_card_type,
                p.credit_card_number,
                ROW(p.amount, IF(p.currency = 0, 'USD', 'USD')))
    END as payment
FROM item_details AS o
    INNER JOIN shipping_location AS s ON s.order_id = o.order_id
    LEFT JOIN payment AS p ON p.order_id = o.order_id
GROUP BY o.order_id, o.item_status, s.customer_name, s.customer_address, s.zip_code,
         s.latitude, s.longitude, p.payment_type, p.credit_card_type, p.credit_card_number,
         p.currency, p.amount;