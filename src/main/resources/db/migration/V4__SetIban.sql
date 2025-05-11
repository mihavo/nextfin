UPDATE accounts
SET iban = CONCAT(
        'GR',
        '00', -- Placeholder for check digits
        '12', -- Bank code (fixed length)
        '00000000140000', -- Branch code padded to 14 digits (example assumes branch code '14')
        LPAD(id::text, 20, '0') -- Account number padded to 20 digits
           )
WHERE iban IS NULL;